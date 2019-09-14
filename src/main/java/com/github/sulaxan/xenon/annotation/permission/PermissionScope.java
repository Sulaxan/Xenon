package com.github.sulaxan.xenon.annotation.permission;

/**
 * Represents the scope of permission methods.
 *
 * @see PermissionCheck for further insights.
 */
public enum PermissionScope {

    /**
     * Indicates the scope of the permission to check the root
     * command.
     */
    ROOT,

    /**
     * Indicates the scope of the permission to check specific
     * sub commands.
     */
    SUB_COMMAND
}
