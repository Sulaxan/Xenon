package com.github.sulaxan.xenon.data.mapping;

public interface CommandMethodMapping extends MethodMapping {

    Class[] getParameterTypes();

    boolean includeArgs();
}
