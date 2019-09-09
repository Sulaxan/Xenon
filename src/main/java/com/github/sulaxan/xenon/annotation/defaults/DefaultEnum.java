package com.github.sulaxan.xenon.annotation.defaults;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DefaultValue
public @interface DefaultEnum {

    String value();

    Class<? extends Enum> valueClass();
}
