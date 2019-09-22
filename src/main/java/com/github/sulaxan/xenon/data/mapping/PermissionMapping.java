package com.github.sulaxan.xenon.data.mapping;

import com.github.sulaxan.xenon.annotation.permission.PermissionCheck;

import java.lang.reflect.Method;

/**
 * Implementation of {@link PermissionMethodMapping} used to identify
 * permission checking methods.
 */
public class PermissionMapping implements PermissionMethodMapping {

    private Method method;
    private PermissionCheck check;

    public PermissionMapping(Method method, PermissionCheck check) {
        this.method = method;
        this.check = check;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public PermissionCheck getCheck() {
        return check;
    }
}
