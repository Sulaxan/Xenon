package com.github.sulaxan.xenon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents that the class declaring this annotation is a command
 * class. All command classes must have this annotation, otherwise
 * the runtime parser will discard the class as a normal class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {

    /**
     * The names of the command. Names must be of size 1 or greater,
     * and any further specified names will be treated as aliases.
     *
     * @return All possible names used to call the command.
     */
    String[] names();

    /**
     * The description of the command. This value is optional, default
     * implementation will return an empty string.
     *
     * @return The description of the command.
     */
    String desc() default "";
}
