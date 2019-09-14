package com.github.sulaxan.xenon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an option a command may have. All options start with "-"
 * for the short option and "--" for the long option.
 *
 * e.g., -i, --interactive
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Option {

    /**
     * @return The short option value
     */
    String value();

    /**
     * The long option value, usually complimenting the short option
     * value.
     *
     * @return The long option value.
     */
    String longOption() default "";

    /**
     * @return Whether this option is required. False by default.
     */
    boolean required() default false;

    /**
     * @return The description of the option. Empty string by default.
     */
    String desc() default "";
}
