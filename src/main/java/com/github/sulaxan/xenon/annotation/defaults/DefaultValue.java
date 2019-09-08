package com.github.sulaxan.xenon.annotation.defaults;

import com.github.sulaxan.xenon.annotation.defaults.internal.DefaultEnum;

public @interface DefaultValue {

    String valueString() default "";

    int valueInt() default 0;

    long valueLong() default 0;

    float valueFloat() default 0.0f;

    double valueDouble() default 0.0d;

    // class of enum
    Class<? extends Enum> valueEnumClass() default DefaultEnum.class;

    // value of enum as a string (will be processed during command processing)
    String valueEnum() default "NULL";
}
