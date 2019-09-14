package com.github.sulaxan.xenon.manager;

import com.github.sulaxan.xenon.data.CommandData;
import com.github.sulaxan.xenon.sender.CommandSender;

import java.util.List;

/**
 * Represents a manager that processes the registering, fetching, and
 * executing of commands.
 */
public abstract class CommandManager {

    /**
     * Registers the specified command class. The specified class must
     * have the {@link com.github.sulaxan.xenon.annotation.Command} annotation.
     * Furthermore, the declaration of this annotation will allow the command
     * to be parsed without issues, however, it will have no functionality.
     *
     * @param commandClass The class to register.
     * @return {@link RegisterBuilder} that allows specific options to be set.
     *
     * @see CommandData for further information on the structure of command
     * classes.
     */
    public abstract RegisterBuilder register(Class<?> commandClass);

    /**
     * @return All currently registered and parsed commands.
     */
    public abstract List<CommandData> getCommandData();

    /**
     * Finds a command using the specified command.
     *
     * @param command The {@link CommandData} corresponding to command.
     * @return The associated {@link CommandData}, or null if none.
     * @deprecated The implementation of this method, and particularly the
     * usefulness are not fully determined. Usage of this method is
     * discouraged.
     */
    public abstract CommandData findCommand(String command);

    /**
     * Executes the command by first parsing the input and building the
     * command object using the specified values. Depending on implementation,
     * output and errors may be sent to the {@link CommandSender}, or simply
     * logged to the console or thrown as an {@link RuntimeException}.
     *
     * @param sender The {@link CommandSender} issuing the command.
     * @param command The full command line input.
     */
    public abstract void execute(CommandSender sender, String command);
}
