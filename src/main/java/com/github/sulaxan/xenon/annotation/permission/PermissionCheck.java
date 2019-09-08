package com.github.sulaxan.xenon.annotation.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionCheck {

    PermissionScope scope() default PermissionScope.SUB_COMMAND;

    String[] subCommands() default {};
}
