package com.jay.netease.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)//该注解作用在属性上
@Retention(RetentionPolicy.CLASS)
public @interface BindView {
    int value();
}
