package cat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import java.io.IOException;

public class RedueCompressRunner implements Tool {
    private Configuration conf = null;

    /*
reduce 端压缩

输入
a b c
v b
a d a

输出
BZh91AY&SY�N�  ɀ 08 <    1 0 z����]��B@E:�
 */
    public static void main(String[] args) {
        args = new String[]{"bigdata/src/main/resources/words", "bigdata/src/main/resources/out"};

        try {
            int code = ToolRunner.run(new RedueCompressRunner(), args);
            if (code == 0) {
                System.out.println("success");
            } else {
                System.out.println("fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        conf = this.getConf();
        conf.set("inputPath", args[0]);
        conf.set("outputPath", args[1]);

        Job job = Job.getInstance(conf);

        job.setJarByClass(RedueCompressRunner.class);
        job.setMapperClass(WordcountMapper.class);
        job.setReducerClass(WordcountReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 设置reduce端输出压缩开启
        FileOutputFormat.setCompressOutput(job, true);
        // 设置压缩的方式
        FileOutputFormat.setOutputCompressorClass(job, BZip2Codec.class);
//	    FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
//	    FileOutputFormat.setOutputCompressorClass(job, DefaultCodec.class);

        HDFSUtils.initJobInputPath(job);
        HDFSUtils.initJobOutputPath(job);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;

    }

    @Override
    public Configuration getConf() {
        return this.conf;
    }

    static class WordcountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        Text k = new Text();
        IntWritable v = new IntWritable(1);

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] words = line.split(" ");
            for (String word : words) {
                k.set(word);
                context.write(k, v);
            }
        }
    }

    static class WordcountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        IntWritable v = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) {
                sum += value.get();
            }
            v.set(sum);
            context.write(key, v);
        }
    }


}
