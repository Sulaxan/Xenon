package com.github.sulaxan.xenon.data.mapping;

import com.github.sulaxan.xenon.annotation.permission.PermissionCheck;

/**
 * Implementation of {@link MethodMapping} used to indicate a method
 * represents a permission check.
 */
public interface PermissionMethodMapping extends MethodMapping {

    /**
     * @return The permission check information.
     */
    PermissionCheck getCheck();
}
