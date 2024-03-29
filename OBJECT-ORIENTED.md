

面向对象特性



## Exception

checked exception

```java
public class CheckedException {
    public static void main(String[] args) {
        File file = new File("");
        try {
            // invoke chains: createNewFile() -> createFileExclusively()
            // -> native createFileExclusively() throws IOException

            // Exception class: IOException -> Exception -> Throwable

            // IOException checked exception: must try catch or throws
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

```



unchecked exception

```java
public class UncheckedException {
    public void foo() {


        // Exception class: IllegalArgumentException -> RuntimeException -> Exception -> Throwable

        // happened in runtime with unknown condition, parameters and ...

        throw new IllegalArgumentException();
    }
}
```



throws

```java
public class KeywordThrows {
    // throws: able to throw multiple checked exception and unchecked exception on method name
    public void foo() throws NoSuchFieldException, FileNotFoundException, IllegalArgumentException {
    }
}

```



throw

```java
public class KeywordThrow {
    // throw: throw checked exception and unchecked exception on method body

    public void foo() {
        throw new IllegalArgumentException();
    }
    public void foo2() throws FileNotFoundException {
        throw new FileNotFoundException();
    }
}

```



## Generic

类使用泛型

```java
// 类中出现的泛型T都保持同一个类型
public class GenericClass<T> {
    private T x;

    public void foo(T value) {}
    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }
}
```



接口使用泛型

```java
// 定义泛形型接口
public interface GenericInterface<T> {
    T next();
}
```



方法使用泛型

```java
public class GenericMethod {
    // 定义泛型方法
    public <T> void foo(T x) {

    }
}
```



泛型的继承关系

```java
public class GenericExtends {

    public static void main(String[] args) {
        GenericExtends o = new GenericExtends();
        o.fromArrayToList(new Integer("1"));

        List<Number> dest = new ArrayList<>();
        List<Integer> src = new ArrayList<>();
        // Number super G; Integer extends G
        copy(dest, src);
    }

    // 参数类型T，决定了返回类型 List<T>；参数泛型T 必须是Number 的子类
    public <T extends Number> List<T> fromArrayToList(T a) {
        return null;
    }

    // 参数 dest的泛型 是G 的超类，参数 src 的泛型是G 的子类，所以 dest 的泛型是 src泛型的父类
    public static <G> void copy(List<? super G> dest, List<? extends G> src) {
    }
}
```



## Abstract

```java
public abstract class KeywordAbstract {
    // abstract class 可以定义变量
    private String s;
    // abstract class 可以定义静态变量
    private static String b;

    // abstract class 可以定义构造方法
    KeywordAbstract() {}

    // abstract class 可以定义抽象方法
    // 如果一个类有抽象方法，就一定是抽象类
    abstract void foo();

    // abstract class 可以定义具体方法
    public void concrete() {}

    // abstract class 可以定义静态方法
    public static void concreteS() {}

    public static void main(String[] args) {
        // abstract class 不能实例化
//        KeywordAbstract o = new KeywordAbstract();
    }
}
```



## Final

不能继承 final 修饰的类

```java
// 不能继承 final 修饰的类
public class ExtendFinalClass
//        extends KeywordFinalClass
{ }
```



final 修饰的类

```java
// final 修饰的类
public final class KeywordFinalClass {
    // final 类的非final 全局变量可以修改，没有被隐式修饰为final
    public String s = "aa";

    // final 类的非final 方法 隐式指定为 final
    public void foo() {}

    public static void main(String[] args) {
        KeywordFinalClass o = new KeywordFinalClass();
        o.s = "update";
        System.out.println(o.s);
    }
}
```



父类 final 方法

```java
// 父类 final 方法
public class KeywordFinalMethod extends KeywordFinalMethodParent{
    // 父类 final 方法不能被重写
//    public void foo() {}
}

class KeywordFinalMethodParent {
    public final void foo() {
    }
}
```



final 修饰变量

```java
// final 修饰变量
public class KeywordFinalVar {
    public final String a = "a";
    public final Object[] arr = {1,2};

    public static void main(String[] args) {
        KeywordFinalVar o = new KeywordFinalVar();
        // final 修饰的基本数据类型不可修改
//        o.a = "aaa";

        // final 修饰的引用类型，对象堆内存的值可变
        o.arr[0] = 3;
        System.out.println(o.arr[0]);

        // final 修饰的引用类型，地址不可变
        Object[] arr2 = {5,6};
//        o.arr = arr2;
    }
}
```



## Interface

接口

```java
public interface InterfaceDefined {
    // 接口不能实例化，没有构造方法
//    InterfaceDefined ();

    void foo();

    // public 方法没有方法体
//    void bar() {};

    public void foo1();

    // default 同一个包的类可访问
    default void foo3() {}

}
```



实现接口

```java
// implements 接口需要实现接口的全部方法
public class implementInterface implements InterfaceDefined {

    @Override
    public void foo() {

    }

    @Override
    public void foo1() {

    }

    @Override
    public void foo3() {

    }
}
```



## Lock



CAS

```java
// CAS 模拟乐观锁
public class CAS {
    int count = 0;

    // unsafe CAS 在多线程下同时修变量count，只有内存值与 expected 值相等的线程能成功修改 count，即取得了锁
    public boolean cas(int expected, int x) {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe unsafe = (Unsafe) theUnsafe.get(Unsafe.class);
            long offset = unsafe.objectFieldOffset(this.getClass().getDeclaredField("count"));
            // expected: 当前值的预期值，如果内存的值不是预期值，说明被其他线程修改了，就返回 false
            // x: 要修改成的值
            return unsafe.compareAndSwapInt(this, offset, expected, x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean lock() {
        return cas(0, 1);
    }

    public int getCount() {
        return count;
    }

    public boolean isLock() {
        return count != 0;
    }

    public boolean release() {
        return cas(1, 0);
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        CAS o = new CAS();

        Runnable runnable = () -> {
            boolean success = o.lock();
            System.out.printf("thread=%s, count=%s, success=%s%n", Thread.currentThread().getName(), o.getCount(), success);
        };

        // 多线程修改共享资源，cas判断资源是否被修改过
        for (int i = 0; i < 100; i++) {
            new Thread(runnable).start();
        }

    }
}
```



ReentrantLock 测试锁重入

```java
// ReentrantLock 测试锁重入
public class EnterLock {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        Runnable runnable = () -> {
            String name = Thread.currentThread().getName();
            System.out.println(name + " call");
            try {
                lock.lock();
                System.out.printf("1 tread=%s, holdCount=%s%n", name, lock.getHoldCount());
                // 一个线程获取某个对象的锁，可再次获取此对象的锁
                lock.lock();
                System.out.printf("2 tread=%s, holdCount=%s%n", name, lock.getHoldCount());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                lock.unlock();
            }
        };

        new Thread(runnable).start();
    }
}
```



ReentrantLock 测试独占锁/悲观锁

```java
// ReentrantLock 测试独占锁/悲观锁
public class ExcludeLock {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        Runnable runnable = () -> {
            String name = Thread.currentThread().getName();
            System.out.printf("thread=%s, before get lock%n", name);
            try {
                lock.lock();
                System.out.printf("thread=%s, get lock%n", name);

                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.printf("thread=%s, release lock%n", name);

            }
        };

        // 线程独占资源，其他线程访问这个资源时阻塞，直到资源被线程释放其他线程才能竞争资源
        new Thread(runnable).start();
        new Thread(runnable).start();
    }
}
```



公平锁按先后获取锁

```java
// 公平锁按先后获取锁
public class FairLock {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock(true);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    System.out.println(finalI + " before get lock");
                    lock.lock();
                    System.out.println(finalI + " get lock");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }).start();
        }
    }
}
```



非公平锁，先来的未必先抢到锁

```java
// 非公平锁，先来的未必先抢到锁
public class NonFairLock {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock(false);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    System.out.println(finalI + " before get lock");
                    lock.lock();
                    System.out.println(finalI + " get lock");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }).start();
        }
    }
}
```



测试读写锁，多线程可以同时获取读锁，写锁只能被线程独占

```java
// 测试读写锁，多线程可以同时获取读锁，写锁只能被线程独占
public class ReadWriteLock {
    public static void main(String[] args) {
        //Suspend Thread
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        Runnable readTask = () -> {
            //Suspend Thread
            ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
            //Suspend Thread
            readLock.lock();
            try {
                for (int i = 1; i < 11; i++) {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName() + " read " + i + "s");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
            }
        };

        Runnable writeTask = () -> {
            ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
            //Suspend Thread
            writeLock.lock();
            try {
                for (int i = 1; i < 11; i++) {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName() + " write " + i + "s");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
            }
        };
        
        new Thread(readTask).start();
        new Thread(readTask).start();
        new Thread(readTask).start();

        new Thread(writeTask).start();
        new Thread(writeTask).start();
        new Thread(writeTask).start();
    }
}
```



共享锁，多个线程可以同时获取锁

```java
// 共享锁，多个线程可以同时获取锁
public class ShareLock {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);

        Runnable runnable = () -> {
            try {
                semaphore.acquire();
                for (int i = 1; i < 5; i++) {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName() + " read " + i + "s");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        };

        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
    }
}
```



简单自旋锁

```java
// 简单自旋锁
public class SpinLock {
    /**
     * 使用Owner Thread作为同步状态
     */
    private AtomicReference<Thread> sign = new AtomicReference<>();

    /**
     * reentrant count of a thread, no need to be volatile
     */
    public void lock() throws InterruptedException {
        Thread t = Thread.currentThread();
        // 自旋
        while (!sign.compareAndSet(null, t)) {
            System.out.println(t.getName() + " spin");
            Thread.sleep(1000);
        }
    }

    public void unlock() {
        Thread t = Thread.currentThread();
        sign.compareAndSet(t, null);
    }

    public static void main(String[] args) {
        SpinLock lock = new SpinLock();

        Runnable runnable = () -> {
            String name = Thread.currentThread().getName();
            try {
                lock.lock();
                System.out.println(name + " obtain lock");
                for (int i = 1; i < 5; i++) {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(name + " doing task " + i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println(name + " unlock");
            }
        };

        new Thread(runnable).start();
        new Thread(runnable).start();
    }
}
```



## Inner Class

非静态内部类特性

```java
public class DefinedInnerNonStaticClass {
    public static void main(String[] args) {
        // 外部类访问内部类
        InnerNonStaticClass n = new DefinedInnerNonStaticClass().new InnerNonStaticClass();
    }

    public int i = 1;
    public void ofoo() {}

    class InnerNonStaticClass {
        // 非静态内部类不能有静态变量
//        public static String a = "a";
        public String b = "b";
        // 非静态内部类可以定义 final static，放常量池
        public final static String c = "c";

        // 非静态内部类不能有静态方法
//        public static void foo1(){}

        // 非静态内部类访问外部类的非静态变量
        public int df = i;

        public void foo2(){
            // 非静态内部类访问外部类非静态方法
            ofoo();
        }
    }
}
```



静态内部类特性

```java
// 静态内部类特性
public class DefinedInnerStaticClass {

    public static void main(String[] args) {
        // 可以直接创建静态内部类，不需要先创建外部类
        InnerStaticClass o = new InnerStaticClass();
        // 先实例化静态内部类再访问内部类非静态变量
        int a = new InnerStaticClass().innerNonStaticVar;
        // 访问静态内部类静态变量不需要实例化
        int b = InnerStaticClass.innerStaticVar;
    }

    public int outerNonStaticVar = 1;
    public static int outerStaticVar = 1;
    public void outerNonStaticMethod() {}
    public static void outerStaticMethod() {}

    static class InnerStaticClass {
        // 静态内部类定义静态全局变量和非静态全局变量
        private int innerNonStaticVar = 3;
        private static int innerStaticVar = 4;
        // 静态内部类不能使用外部类的非静态变量
//        public int a = outerNonStaticVar;
        // 静态内部类不能使用外部类的静态变量
        public int b = outerStaticVar;

        // 静态内部类可以定义静态方法
        public static void innerStaticMethod(){
        }
        // 静态内部类可以定义非静态方法
        public void innerNonStaticMethod(){
            outerStaticMethod();
        }
    }
}
```



局部内部类

```java
public class MethodInnerClass {
    public void test() {
        // 局部内部类
        class InnerClass {
            private String name;
            final static String test = "1";
            public InnerClass(String name) {
                super();
                this.name = name;
            }
            public void say(String str) {
                System.out.println(name+":"+str);
            }
        }
        new InnerClass("test").say("hello");
    }
}
```



## Enum

```java
public enum ColorEnum {
    // 定义枚举对象
    RED("1", "红色"), GREEN("2", "绿色"), BLANK("3", "白色"), YELLOW("4", "黄色");

    private final String code;
    private final String chinese;

    ColorEnum(String code, String chinese) {
        this.code = code;
        this.chinese = chinese;
    }

    public String getCode() {
        return code;
    }

    public String getChinese() {
        return chinese;
    }

    public static void main(String[] args) {
        // 获取全部枚举对象
        ColorEnum[] values = ColorEnum.values();

        // 根据名字获取枚举对象
        ColorEnum red1 = ColorEnum.valueOf("RED");
        ColorEnum red2 = ColorEnum.RED;

        // 获取枚举索引
        int index = red1.ordinal();
    }

}
```



## Construct

```java
public class ConstructDefined {
    private int i;
    private int j;

    public ConstructDefined(int i) {
        this.i = i;
    }

    public ConstructDefined(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public static void main(String[] args) {
        // 报错，定义了有参构造方法时，不提供无参构造方法
//        new ConstructDefined();
    }
}
```



```java
public class ConstructNotDefined {
    public static void main(String[] args) {
        // 未定义构造方法时，提供无参构造方法
        new ConstructNotDefined();
    }
}
```



## static

```java
public class KeywordStatic {
    public static void main(String[] args) {
        DefinedStaticMethodAndValue o = new DefinedStaticMethodAndValue();
        System.out.println(DefinedStaticMethodAndValue.s);
        DefinedStaticMethodAndValue.foo();
    }
}

class DefinedStaticMethodAndValue {
    // 修饰代码块，可用于类的初始化操作，提升程序的性能
    static {
        System.out.println("static block");
    }

    // static 修饰的成员变量为静态成员变量，生命周期和类相同，在整个程序执行期间都有效
    public static String s = "str";

    // static 修饰的方法为静态方法，能直接调用；静态方法不依赖任何对象就可以直接访问
    public static void foo() {
        System.out.println("foo");
    }
}
```



## Orders



代码块的加载顺序

```java
// orders: static block -> not static block -> construct
public class BlockOrders {
    private static String staticValue = "a";
    private String notStaticValue = "a";

    static {
        System.out.println("static block");
        // 静态代码块能访问静态变量
        String s1 = staticValue;
        // 静态代码块不能访问非静态变量
//        String s2 = notStaticValue;
    }

    {
        System.out.println("not static block");
        // 非静态代码块能访问静态变量
        String s1 = staticValue;
        // 非静态代码块能访问非静态变量
        String s2 = notStaticValue;
    }

    public BlockOrders() {
        System.out.println("construct");
    }

    public static void main(String[] args) {
        new BlockOrders();
    }
}
```



父子类代码块和构造函数的执行顺序

```java
// 父子类代码块和构造函数的执行顺序
public class ExtendsOrders {
    public static void main(String[] args) {
        //静态代码块只加载一次
        new ChildOrders();
        System.out.println("-----------------再次创建子类-----------------");
        new ChildOrders();
    }
}

class ParentOrders {
    static {
        System.out.println("父类静态代码块");
    }

    {
        System.out.println("父类代码块");
    }

    ParentOrders() {
        System.out.println("父类构造函数");
    }
}

class ChildOrders extends ParentOrders {
    static {
        System.out.println("子类静态代码块");
    }

    {
        System.out.println("子类代码块");
    }

    public ChildOrders() {
        System.out.println("子类构造函数");
    }
}
```



## Method

方法定义

```java
public class MethodDefined {
    /*
    public      访问修饰符
    static      静态方法
    void        返回值类型
    main        方法名
    String[]    参数类型
    args        参数
    {}          方法体
     */
    public static void main(String[] args) {

    }
}
```



方法重载

```java
// 方法重载
public class MethodOverload {
    // 方法参数类型不同，可重载
    public void foo(int i) {}
    public void foo(String s) {}

    // 方法参数个数不同，可重载

    // 方法参数排序不同，可重载
    public void foo(int i, String s) {}
    public void foo(String s, int i) {}

}
```



方法重写

```java
// 方法重写
public class MethodOverride {
    public static void main(String[] args) {
        System.out.println(new Child().bar(""));
    }
}

class Parent {
    public String bar(String s) {
        return "parent";
    }
}

class Child extends Parent{
    // 方法重写
    // 返回值类型、方法名和参数列表 与父类方法保持一致
    // @Override 表示重写
    // 访问修饰符权限 public 不能小于父类
    @Override
    public String bar(String s) {
        return "child";
    }
}
```



## Polymorphism

```java
// 多态
public class Polymorphism {
    public static void main(String[] args) {
        Fruit fruit = new Apple();
        // 多态，父类引用指向子类对象
        fruit.eat();
    }
}

class Fruit {
    int num;
    public void eat() {
        System.out.println("eat fruit");
    }
}

class Apple extends Fruit {
    @Override
    public void eat() {
        super.num = 10;
        System.out.println("eat " + num + " apple");
    }
}
```



## Class

```java
// Class 对象的构成
public class DefinedClass {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchFieldException {
        Class clz = getClassByInstance();
        
        Field[] fields = clz.getFields();
        Method[] methods = clz.getMethods();
        Annotation[] annotations = clz.getAnnotations();
        ClassLoader classLoader = clz.getClassLoader();
    }

    // 获取 Class 对象 1
    private static Class getClassByInstance() {
        DefinedClass o = new DefinedClass();
        Class<? extends DefinedClass> clz = o.getClass();
        return clz;
    }
    // 获取 Class 对象 2
    private static Class<DefinedClass> getClassByClass() {
        Class<DefinedClass> clz = DefinedClass.class;
        return clz;
    }
    // 获取 Class 对象 3
    private static Class<?> getClassbyName () throws ClassNotFoundException {
        Class<?> clz = Class.forName("com.example.ReflectClass");
        return clz;
    }
}
```



## Super

```java
// keyword Super
public class keywordSuper {
    public static void main(String[] args) {
        new ChildS().bar();
    }
}

class ParentS {
    public String s = "parent string";
    public void foo() {
        System.out.println("parent");
    }
}

class ChildS extends ParentS{
    public ChildS() {
        // 实例化父类
        super();
    }

    public void bar() {
        // super 表示父类对象，可以调父类属性和方法
        System.out.println(super.s);
        super.foo();
    }
}
```



## this

```java
public class KeywordThis {
    private String s = "str";

    public void foo() {
        System.out.println("foo");
    }

    public void bar() {
        // this 表示当前对象，可以调用方法、调用全局变量
        this.foo();
        System.out.println(this.s);
    }

    public static void main(String[] args) {
        new KeywordThis().bar();
    }
}
```



## Annotation

```java
/**
 * 定义一个注解
 * Target ElementType
 *      TYPE 作用对象：类、接口、枚举类
 *      METHOD 作用对象：方法
 *
 * Retention RetentionPolicy
 *      RUNTIME 编译时写到 class，虚拟机运行时创建
 *      SOURCE 编译时移除
 *      CLASS 编译时写到 class，虚拟机运行时不创建
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DefinedAnnotation {
    String value();
}
```



```java
@DefinedAnnotation(value = "aaa")
public class UsingAnnotation {
}
```



```java
public class TestDemo {
    public static void main(String[] args) {
        String value = UsingAnnotation.class.getAnnotation(DefinedAnnotation.class).value();
        System.out.println(value);
    }
}
```



## Clone

浅克隆，不克隆引用类型的变量

```java
// 浅克隆，不克隆引用类型的变量
public class ShallowClone {
    public static void main(String[] args) {
        try {
            ShallowCloneObject obj = new ShallowCloneObject();
            ShallowCloneObject clone = (ShallowCloneObject) obj.clone();

            System.out.println(obj == clone);
            System.out.println(obj.sub == clone.sub);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}

// 对象实现 Cloneable，重写clone()
class ShallowCloneObject implements Cloneable {
    public ShallowCloneSubObject sub;

    public ShallowCloneObject() {
        this.sub = new ShallowCloneSubObject("a");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }
}

class ShallowCloneSubObject implements Serializable {
    private static final long serialVersionUID = -4537716904357183030L;
    public String s;

    public ShallowCloneSubObject(String s) {
        this.s = s;
    }
}
```

深克隆，克隆对象的引用类型变量也被克隆

```java
/**
 * 深克隆，克隆对象的引用类型变量也被克隆
 * 对象 -> 对象流输出 -> 数组输出流 -> 数组输入流 -> 对象输出流
 */
public class DeepClone {
    public static void main(String[] args) throws IOException {
        DeepCloneObject user = new DeepCloneObject();
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(user);
            byteArrayOutputStream.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();

            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                DeepCloneObject clone = (DeepCloneObject) objectInputStream.readObject();
                System.out.println(user == clone);
                // 克隆对象的引用类型变量也被克隆
                System.out.println(user.address == clone.address);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

class DeepCloneObject implements Serializable {
    private static final long serialVersionUID = -3307269962764425802L;
    public DeepCloneSubObject address;

    public DeepCloneObject() {
        this.address = new DeepCloneSubObject("ss");
    }
}


class DeepCloneSubObject implements Serializable {
    private static final long serialVersionUID = -4537716904357183030L;
    public String s;

    public DeepCloneSubObject(String s) {
        this.s = s;
    }
}
```



## IO

```java
public class IOTest {
    /**
     * 文件输入流写 -> 字节数组输出流
     */
    @Test
    public void fileStream2ByteStream() {
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/source");
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            while (true) {
                int readLength = inputStream.read(buffer);
                if (readLength != -1) {
                    byteArrayOutputStream.write(buffer, 0, readLength);
                } else {
                    break;
                }
            }
            String str = byteArrayOutputStream.toString();
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 文件 Reader 输入流 -> 字符数组输出流
     */
    @Test
    public void fileChar2CharArray() {
        try (FileReader fileReader = new FileReader("src/main/resources/source");
             CharArrayWriter charArrayWriter = new CharArrayWriter()) {
            char[] buffer = new char[1024];
            while (true) {
                int readLength = fileReader.read(buffer);
                if (readLength != -1) {
                    charArrayWriter.write(buffer, 0, readLength);
                } else {
                    break;
                }
            }
            System.out.println(charArrayWriter.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件 Writer 输出流
     */
    @Test
    public void fileChar() {
        try (Writer out = new FileWriter("src/main/resources/output")) {
            out.write("长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串长字符串");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件输入流 -> 文件输出流
     */
    @Test
    public void fileStream() {
        try (InputStream in = new FileInputStream("src/main/resources/source");
             OutputStream out = new FileOutputStream("src/main/resources/source_cp")) {
            byte[] b = new byte[1024];
            while (true) {
                int len = in.read(b);
                if (len != -1) {
                    out.write(b, 0, len);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 字节数组 -> 文件输出流
     */
    @Test
    public void fileOutputStream() {
        try (OutputStream out = new FileOutputStream("src/main/resources/output")) {
            out.write(new byte[]{65, 66});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
```

