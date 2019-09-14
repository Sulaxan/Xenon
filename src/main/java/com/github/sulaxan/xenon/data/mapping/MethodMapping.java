package com.github.sulaxan.xenon.data.mapping;

import java.lang.reflect.Method;

/**
 * Method mappings are used to indicate the properties of specific
 * methods in a command class.
 */
public interface MethodMapping {

    /**
     * @return The target method.
     */
    Method getMethod();
}
