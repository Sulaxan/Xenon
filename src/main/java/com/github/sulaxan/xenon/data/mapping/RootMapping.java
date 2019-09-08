package com.github.sulaxan.xenon.data.mapping;

import com.github.sulaxan.xenon.annotation.Root;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class RootMapping implements MethodMapping {

    private Method method;
    private Root root;

    public RootMapping(Method method, Root root) {
        this.method = method;
        this.root = root;
    }
}
