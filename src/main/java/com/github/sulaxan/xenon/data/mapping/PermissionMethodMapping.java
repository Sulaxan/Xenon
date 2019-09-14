package com.github.sulaxan.xenon.data.mapping;

import com.github.sulaxan.xenon.annotation.permission.PermissionCheck;

import java.lang.reflect.Method;

public interface PermissionMethodMapping extends MethodMapping {

    Method getMethod();

    PermissionCheck getCheck();
}
