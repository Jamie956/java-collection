package com.jamie;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Convert {
    @Test
    public void getDateString() {
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withZone(ZoneId.systemDefault()).format(Instant.now().minus(1, ChronoUnit.DAYS));

        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 获取当前日期的前一天
        String yesterday = today.plusDays(-1).toString();
    }

    //html 转义
    @Test
    public void htmlConvert(){
        String str = "&nbsp;-&lt;-&gt;-&amp;-&quot;-&apos;";

        String unescapeHtml4 = StringEscapeUtils.unescapeHtml4(str);
        System.out.println(unescapeHtml4);
        String escapeHtml4 = StringEscapeUtils.escapeHtml4(unescapeHtml4);
        System.out.println(escapeHtml4);

        String unescapeHtml3 = StringEscapeUtils.unescapeHtml3(str);
        System.out.println(unescapeHtml3);
        String escapeHtml3 = StringEscapeUtils.escapeHtml3(unescapeHtml3);
        System.out.println(escapeHtml3);
    }

    /**
     * 驼峰、下划线字符串转换 by guava
     */
    @Test
    public void guavaConvert() {
        // 驼峰转下划线, userName -> user_name
        Converter<String, String> converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);
        // 输出: user_name
        System.out.println(converter.convert("userName"));
        System.out.println(converter.convert("user_name"));

        // 驼峰转连接符, userName -> user-name
        converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN);
        // 输出: user-name
        System.out.println(converter.convert("userName"));

        // 驼峰转首字符大写驼峰, userName -> UserName
        converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL);
        // 输出: UserName
        System.out.println(converter.convert("userName"));

        // 驼峰转大写下划线, userName -> USER_NAME
        converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE);
        // 输出: USER_NAME
        System.out.println(converter.convert("userName"));
    }

}