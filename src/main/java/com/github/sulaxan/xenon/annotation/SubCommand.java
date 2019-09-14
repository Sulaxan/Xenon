package com.github.sulaxan.xenon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a method to be a sub command.
 *
 * E.g.,
 * <p>
 *     <em>@SubCommand("subcommand")</em>
 *     public void aSubCommand(CommandSender sender) {
 *     }
 *
 *     // OR
 *
 *     <em>@SubCommand("subcommand")</em>
 *     public void roo2(CommandSender sender, String[] args) {
 *     }
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommand {

    /**
     * @return The name of the sub command.
     */
    String value();
}
