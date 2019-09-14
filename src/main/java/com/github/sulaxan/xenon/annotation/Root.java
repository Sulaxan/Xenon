package com.github.sulaxan.xenon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a method to be the root method, or simply, the default
 * method to be called if a sub command does not exist. This annotation
 * must exist on one method, otherwise the runtime parser will throw a
 * {@link com.github.sulaxan.xenon.exception.CommandParseException}.
 *
 * E.g.,
 *
 * <p>
 *     <em>@Root</em>
 *     public void root(CommandSender sender) {
 *     }
 *
 *     // OR
 *
 *    <em>@Root</em>
 *    public void roo2(CommandSender sender, String[] args) {
 *    }
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Root {

}
