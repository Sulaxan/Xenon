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
import org.apache.commons.cli.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GenericCommandManager extends CommandManager {

    private List<DefaultCommandData> commandData = Lists.newCopyOnWriteArrayList();
    private CommandLineParser parser = new DefaultParser();

    public GenericCommandManager() {
    }

    @Override
    public RegisterBuilder register(Class<?> commandClass) {
        return new DefaultRegisterBuilder().withCommandClass(commandClass);
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

        try {
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
                Object[] constructorArgs = data.getConstructorArgs().call();
                Class<?>[] classes = new Class[constructorArgs.length];
                for(int i = 0; i < constructorArgs.length; i++) {
                    classes[i] = constructorArgs[i].getClass();
                }

                Object commandObj = data.getCommandClass().getConstructor(classes).newInstance(constructorArgs);

                // Parse the command using Apache's CommandLineParser
                CommandLine line = parser.parse(data.getOptions(), args, false);
                for(OptionMapping mapping : data.getOptionMappings()) {
                    String opt = line.getOptionValue(mapping.getOption().getOpt(), null);
                    // Throw parse exception if the option doesn't exist and is required
                    if(mapping.getOption().isRequired() && opt == null)
                        throw new CommandParseException("Option " + mapping.getOption().getOpt() +
                                " is required but does not exist");

                    // Set the field in the command object
                    setField(
                            mapping.getField(),
                            commandObj,
                            opt != null ? parseObject(mapping.getField().getType(), opt) :
                                    mapping.getDefaultValue()
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
                        throw new CommandParseException("Insufficient permissions");
                }


                // Check if the parameter count and arg count from command are equal
                // This count excludes the sender object and the optional String[] args object
                if(callMethod.getParameterTypes().length != commandArgs.length)
                    throw new CommandParseException("Specified arg count and method arg count don't match");

                // Add all objects required for the method to be invoked
                List<Object> methodArgs = Lists.newArrayList(sender);
                for(int i = 0; i < callMethod.getParameterTypes().length; i++) {
                    methodArgs.add(parseObject(callMethod.getParameterTypes()[i], commandArgs[i]));
                }

                // Add the args if required
                if(callMethod.includeArgs())
                    methodArgs.add(args);

                // Finally, call the command
                callMethod.getMethod().invoke(commandObj, methodArgs.toArray());

                // break; so we don't parse another command
                break;
            }
        } catch (Exception e) {
            throw new CommandParseException(e.getMessage());
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

    private Object parseObject(Class clazz, String value) {
        if(clazz == Byte.class) {
            return Byte.valueOf(value);
        } else if(clazz == Short.class) {
            return Short.valueOf(value);
        } else if(clazz == Integer.class) {
            return Integer.valueOf(value);
        } else if(clazz == Long.class) {
            return Long.valueOf(value);
        } else if(clazz == Float.class) {
            return Float.valueOf(value);
        } else if(clazz == Double.class) {
            return Double.valueOf(value);
        } else if(clazz == String.class) {
            return value;
        } else if(clazz.isEnum()) {
            return matchEnum(clazz, value);
        }
        return null;
    }

    private <T extends Enum> T matchEnum(Class<T> clazz, String value) {
        for(T constant : clazz.getEnumConstants()) {
            try {
                if (constant.name().equalsIgnoreCase(value))
                    return constant;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
