package com.github.sulaxan.xenon.data.mapping;

import com.github.sulaxan.xenon.annotation.SubCommand;

/**
 * Implementation of {@link MethodMapping} used to indicate a method
 * represents either the root or a sub command.
 *
 * @see MethodMapping for details on mappings.
 * @see com.github.sulaxan.xenon.annotation.Root for information on root methods.
 * @see SubCommand for information on sub command methods.
 */
public interface CommandMethodMapping extends MethodMapping {

    /**
     * @return The {@link com.github.sulaxan.xenon.sender.CommandSender} class.
     * This class can be any subclass of CommandSender, or simply just CommandSender.
     */
    Class<?> getCommandSenderClass();

    /**
     * @return Whether to include String[] as an argument to the method.
     */
    boolean includeArgs();

    /**
     * @return Whether the method is a sub command or not.
     */
    boolean isSubCommand();

    /**
     * @return The sub command information if it exists, null otherwise.
     */
    SubCommand getSubCommand();
}
