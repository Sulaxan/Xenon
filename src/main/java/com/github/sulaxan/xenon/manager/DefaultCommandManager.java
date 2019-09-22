package com.github.sulaxan.xenon.manager;

import com.github.sulaxan.xenon.command.HelpCommand;
import com.github.sulaxan.xenon.data.CommandData;
import com.github.sulaxan.xenon.data.mapping.CommandMethodMapping;
import com.github.sulaxan.xenon.data.mapping.MethodMapping;
import com.github.sulaxan.xenon.data.mapping.OptionMapping;
import com.github.sulaxan.xenon.exception.CommandNotFoundException;
import com.github.sulaxan.xenon.exception.CommandParseException;
import com.github.sulaxan.xenon.exception.NotEnoughPermissionsException;
import com.github.sulaxan.xenon.sender.CommandSender;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.cli.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Default implementation for {@link CommandManager}.
 */
@Getter
public class DefaultCommandManager extends CommandManager {

    private List<CommandData> commandData = Lists.newCopyOnWriteArrayList();
    private CommandLineParser parser = new DefaultParser();

    public DefaultCommandManager() {
    }

    @Override
    public RegisterBuilder register(Class<?> commandClass) {
        return new DefaultRegisterBuilder(this).withCommandClass(commandClass);
    }

    @Override
    public List<CommandData> getCommandData() {
        return commandData;
    }

    @Override
    public CommandData findCommand(String command) throws CommandParseException {
        String[] args = command.split("\\s");
        for(CommandData data : commandData) {
            if(data.getName().equalsIgnoreCase(args[0]))
                return data;

            // Check aliases
            for(String alias : data.getAliases()) {
                if(alias.equalsIgnoreCase(args[0])) {
                    return data;
                }
            }
        }
        return null;
    }

    @Override
    public void execute(CommandSender sender, String command) {
        if(command.length() == 0)
            return;

        if(sender == null)
            return;

        try {
            parseAndRun(sender, command);
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendError("Something went wrong while parsing the command: " +
                    e.getClass().getSimpleName() + " " + e.getMessage());
        }
    }

    /**
     * Internal method to parse a command issued by a sender.
     *
     * @param sender The sender issuing the command.
     * @param command The full command issued.
     * @throws Exception If the command could not be fully parsed and executed.
     */
    private void parseAndRun(CommandSender sender, String command) throws Exception {
        String[] args = command.split("\\s");
        // Match the command with the data
        for(CommandData data : commandData) {
            // Find the alias if the command value couldn't be matched
            if(!data.getName().equalsIgnoreCase(args[0])) {
                boolean found = false;
                for(String alias : data.getAliases()) {
                    if(alias.equalsIgnoreCase(args[0])) {
                        found = true;
                        break;
                    }
                }

                if(!found)
                    continue;
            }

            // Remove the first arg (command portion) from the string
            args = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];

            // Construct the object using the constructor args
            Object[] constructorArgs = new Object[0];
            Class<?>[] classes = new Class[0];
            if(data.getConstructorArgs() != null) {
                constructorArgs = data.getConstructorArgs().call();
                classes = new Class[constructorArgs.length];
                for (int i = 0; i < constructorArgs.length; i++) {
                    classes[i] = constructorArgs[i].getClass();
                }
            }

            Object commandObj = null;

            for(Constructor constructor : data.getCommandClass().getConstructors()) {
                boolean isValid = true;
                if(constructor.getParameterCount() == classes.length) {
                    for(int i = 0; i < classes.length; i++) {
                        try {
                            constructor.getParameterTypes()[i].asSubclass(classes[i]);
                            isValid = false;
                        } catch (Exception e) {
                            // No need to print stack trace since we're just checking to see if they are similar
                        }
                    }
                }

                if(isValid)
                    commandObj = constructor.newInstance(constructorArgs);
            }

            if(commandObj == null)
                throw new CommandParseException("Could not find appropriate constructor for " + args[0]);

            // Parse the command using Apache's CommandLineParser
            CommandLine line = parser.parse(data.getOptions(), args, false);
            for(OptionMapping mapping : data.getOptionMappings()) {
                String opt = line.getOptionValue(mapping.getOption().getOpt(), null);
                // If the arg is required, and it doesn't exist, Apache's Common CLI will throw an exception
                // This means we do not need to handle that ourselves

                // Set the field in the command object
                Object value;
                if(mapping.getField().getType() == String.class) {
                    value = opt;
                } else {
                    value = line.hasOption(mapping.getOption().getOpt());
                }

                setField(
                        mapping.getField(),
                        commandObj,
                        value
                );
            }

            // Get the sub command method mapping if it exists...
            CommandMethodMapping callMethod = null;
            boolean root = true;
            String[] commandArgs = line.getArgs();
            String subCommand = null;
            if(commandArgs.length >= 1) {
                callMethod = data.getSubCommand(commandArgs[0]);
                if(callMethod != null) {
                    subCommand = commandArgs[0];
                    commandArgs = commandArgs.length >= 2 ?
                            Arrays.copyOfRange(commandArgs, 1, commandArgs.length) : new String[0];
                    root = false;
                }
            }

            // ... or default to the root mapping
            if(callMethod == null)
                callMethod = data.getRootMapping();

            // Check permissions
            MethodMapping permissionMapping = root ? data.getRootPermissionMapping() :
                    data.getPermissionMapping(subCommand);

            if(permissionMapping != null) {
                // We can assume it's a boolean since it's checked when the command gets parsed
                boolean hasPermission = (Boolean) permissionMapping.getMethod().invoke(commandObj, sender);

                if(!hasPermission)
                    throw new NotEnoughPermissionsException("Insufficient permissions");
            }

            // Check if the sender provided is a valid class of the call method's command sender
            sender.getClass().asSubclass(callMethod.getCommandSenderClass());
            // Add all objects required for the method to be invoked
            List<Object> methodArgs = Lists.newArrayList(sender);

            // Add the args if required
            if(callMethod.includeArgs())
                methodArgs.add(commandArgs);

            // Finally, call the command
            callMethod.getMethod().invoke(commandObj, methodArgs.toArray());

            // return; so we don't parse another command
            return;
        }

        throw new CommandNotFoundException("Could not parse " + command);
    }

    /**
     * Internal method to set a field.
     *
     * @param field The field to modify.
     * @param object The instance to modify on.
     * @param value The new value of the field.
     */
    private void setField(Field field, Object object, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets whether the default help implementation should be registered.
     *
     * @param manager The command manager to register to.
     * @return The parsed command data.
     *
     * @see HelpCommand for more info on default help implementation.
     */
    public static CommandData enableHelp(CommandManager manager) {
        return manager.register(HelpCommand.class)
                .withConstructorArgs(manager)
                .build();
    }
}
