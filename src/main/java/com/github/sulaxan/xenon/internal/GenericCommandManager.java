package com.github.sulaxan.xenon.internal;

import com.github.sulaxan.xenon.annotation.Command;
import com.github.sulaxan.xenon.data.CommandData;
import com.github.sulaxan.xenon.data.DefaultCommandData;
import com.github.sulaxan.xenon.data.mapping.CommandMethodMapping;
import com.github.sulaxan.xenon.data.mapping.MethodMapping;
import com.github.sulaxan.xenon.data.mapping.OptionMapping;
import com.github.sulaxan.xenon.exception.CommandParseException;
import com.github.sulaxan.xenon.manager.CommandManager;
import com.github.sulaxan.xenon.manager.RegisterBuilder;
import com.github.sulaxan.xenon.sender.CommandSender;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.cli.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Getter
public class GenericCommandManager extends CommandManager {

    private List<DefaultCommandData> commandData = Lists.newCopyOnWriteArrayList();
    private CommandLineParser parser = new DefaultParser();

    public GenericCommandManager() {
    }

    @Override
    public RegisterBuilder register(Class<?> commandClass) {
        return new DefaultRegisterBuilder(this).withCommandClass(commandClass);
    }

    @Override
    public List<DefaultCommandData> getCommandData() {
        return commandData;
    }

    @Override
    public DefaultCommandData findCommand(String command) throws CommandParseException {
        String[] args = command.split("\\s");
        for(DefaultCommandData data : commandData) {
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
            sender.sendError("Something went wrong while parsing command: " +
                    e.getClass().getName() + " " + e.getMessage());
        }
    }

    private void parseAndRun(CommandSender sender, String command) throws Exception {
        String[] args = command.split("\\s");
        // Match the command with the data
        for(CommandData data : commandData) {
            // Find the alias if the command name couldn't be matched
            if(!data.getName().equalsIgnoreCase(args[0])) {
                boolean found = false;
                for(String alias : data.getAliases()) {
                    if(alias.equalsIgnoreCase(args[0])) {
                        found = true;
                        break;
                    }
                }

                if(!found)
                    return;
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

            Object commandObj = data.getCommandClass().getConstructor(classes).newInstance(constructorArgs);

            // Parse the command using Apache's CommandLineParser
            CommandLine line = parser.parse(data.getOptions(), args, false);
            for(OptionMapping mapping : data.getOptionMappings()) {
                String opt = line.getOptionValue(mapping.getOption().getOpt(), null);
                // If the arg is required, and it doesn't exist, Apache Common CLI will throw an exception
                // This means we do not need to handle that ourselves

                // Set the field in the command object
                Object value;
                if(mapping.getField().getType() == String.class) {
                    value = opt;
                } else {
                    value = true;
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
                    throw new RuntimeException("Insufficient permissions");
            }

            // Add all objects required for the method to be invoked
            List<Object> methodArgs = Lists.newArrayList(sender);

            // Add the args if required
            if(callMethod.includeArgs())
                methodArgs.add(commandArgs);

            // Finally, call the command
            callMethod.getMethod().invoke(commandObj, methodArgs.toArray());

            // break; so we don't parse another command
            break;
        }
    }

    private void setField(Field field, Object object, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
