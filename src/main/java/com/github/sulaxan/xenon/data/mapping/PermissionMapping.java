package com.github.sulaxan.xenon.data.mapping;

import com.github.sulaxan.xenon.annotation.permission.PermissionCheck;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class PermissionMapping implements MethodMapping {

    private Method method;
    private PermissionCheck check;

    public PermissionMapping(Method method, PermissionCheck check) {
        this.method = method;
        this.check = check;
    }
}
