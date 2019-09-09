package com.github.sulaxan.xenon.data.mapping;

import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class RootMapping implements MethodMapping {

    private Method method;
    private boolean includeArgs;

    public RootMapping(Method method, boolean includeArgs) {
        this.method = method;
        this.includeArgs = includeArgs;
    }
}
