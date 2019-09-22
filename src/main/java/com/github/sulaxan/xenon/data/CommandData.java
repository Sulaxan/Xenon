package com.github.sulaxan.xenon.data;

import com.github.sulaxan.xenon.data.mapping.CommandMethodMapping;
import com.github.sulaxan.xenon.data.mapping.OptionMapping;
import com.github.sulaxan.xenon.data.mapping.PermissionMethodMapping;
import org.apache.commons.cli.Options;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Represents all data for commands.
 */
public interface CommandData {

    /**
     * @return The class of the command.
     */
    Class<?> getCommandClass();

    /**
     * Command classes instances are created when a user issues a command.
     * Due to this behaviour, classes may have a constructor that is required,
     * and to allow that flexibility, this method allows all required constructor
     * args to be returned by a callable. This functionality allows more dynamic
     * arguments compared to static arguments.
     *
     * @return Callable for all constructor args.
     */
    Callable<Object[]> getConstructorArgs();

    /**
     * @return The name of the command.
     */
    String getName();

    /**
     * @return All aliases for the command, or a String array of size 0 if
     * none.
     */
    String[] getAliases();

    /**
     * @return The description of the command, or an empty String if none.
     */
    String getDescription();

    /**
     * @return All option mappings for the command.
     */
    List<OptionMapping> getOptionMappings();

    /**
     * @return All options added to {@link Options} to be used for parsing.
     */
    Options getOptions();

    /**
     * @return The root method for the command. This will always return a
     * mapping to a method.
     */
    CommandMethodMapping getRootMapping();

    /**
     * @return All sub command mappings for the command.
     */
    List<CommandMethodMapping> getSubCommandMappings();

    /**
     * Gets a sub command by a name.
     *
     * @param subCommand The name of the sub command.
     * @return The corresponding method mapping, or null if none.
     */
    CommandMethodMapping getSubCommand(String subCommand);

    /**
     * @return All permission mappings for the command.
     */
    List<PermissionMethodMapping> getPermissionMappings();

    /**
     * @return The root method's permission mapping, or null if none.
     */
    PermissionMethodMapping getRootPermissionMapping();

    /**
     * Gets a permission mapping for a sub command.
     *
     * @param subCommand The name of the sub command.
     * @return The corresponding method mapping, or null if none.
     */
    PermissionMethodMapping getPermissionMapping(String subCommand);
}
