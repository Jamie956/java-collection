package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
