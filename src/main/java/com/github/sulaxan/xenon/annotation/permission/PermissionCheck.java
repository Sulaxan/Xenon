package com.github.sulaxan.xenon.annotation.permission;

public @interface PermissionCheck {

    PermissionScope scope() default PermissionScope.SUB_COMMAND;

    String[] subCommands() default {};
}
