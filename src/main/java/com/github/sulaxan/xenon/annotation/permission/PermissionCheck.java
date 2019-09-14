package com.github.sulaxan.xenon.annotation.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an annotation to indicate a method checks for command
 * execute permissions. For this annotation to function properly,
 * the permission method declaring this annotation must include a scope
 * (or none to auto assume SUB COMMAND permission checking), and, if
 * applicable, the sub command names indicating which sub commands the
 * method will check for.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionCheck {

    PermissionScope scope() default PermissionScope.SUB_COMMAND;

    String[] subCommands() default {};
}
