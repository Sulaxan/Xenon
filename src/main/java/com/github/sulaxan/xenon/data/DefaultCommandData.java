package com.github.sulaxan.xenon.data;

import com.github.sulaxan.xenon.annotation.Command;
import com.github.sulaxan.xenon.annotation.permission.PermissionScope;
import com.github.sulaxan.xenon.data.mapping.*;
import com.github.sulaxan.xenon.exception.CommandParseException;
import com.google.common.collect.Lists;
import org.apache.commons.cli.Options;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Default implementation for {@link CommandData}.
 */
public class DefaultCommandData implements CommandData {

    private Class<?> commandClass;
    private String name;
    private String[] aliases = new String[0];
    private String description;
    private Callable<Object[]> constructorArgs;

    private CommandMapping rootMapping;
    private PermissionMethodMapping rootPermissionMapping;

    private List<OptionMapping> optionMappings = Lists.newArrayList();

    private List<CommandMethodMapping> subCommandMappings = Lists.newArrayList();
    private List<PermissionMethodMapping> permissionMappings = Lists.newArrayList();

    public DefaultCommandData(Class<?> commandClass, Callable<Object[]> constructorArgs) {
        this.commandClass = commandClass;
        this.constructorArgs = constructorArgs;
    }

    @Override
    public Class<?> getCommandClass() {
        return commandClass;
    }

    @Override
    public Callable<Object[]> getConstructorArgs() {
        return constructorArgs;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<OptionMapping> getOptionMappings() {
        return optionMappings;
    }

    @Override
    public Options getOptions() {
        Options options = new Options();
        for(OptionMapping mapping : optionMappings) {
            options.addOption(mapping.getOption());
        }
        return options;
    }

    @Override
    public CommandMethodMapping getRootMapping() {
        return rootMapping;
    }

    @Override
    public List<CommandMethodMapping> getSubCommandMappings() {
        return subCommandMappings;
    }

    @Override
    public CommandMethodMapping getSubCommand(String subCommand) {
        for(CommandMethodMapping mapping : subCommandMappings) {
            if(mapping.getSubCommand().value().equalsIgnoreCase(subCommand))
                return mapping;
        }
        return null;
    }

    @Override
    public List<PermissionMethodMapping> getPermissionMappings() {
        return permissionMappings;
    }

    @Override
    public PermissionMethodMapping getRootPermissionMapping() {
        return rootPermissionMapping;
    }

    @Override
    public PermissionMethodMapping getPermissionMapping(String command) {
        for(PermissionMethodMapping mapping : permissionMappings) {
            for(String sc : mapping.getCheck().subCommands()) {
                if(sc.equalsIgnoreCase(command))
                    return mapping;
            }
        }
        return null;
    }

    /**
     * Parses the command class using {@link AnnotationParser}.
     */
    public void parse() {
        try {
            if(commandClass.isAnnotationPresent(Command.class)) {
                Command command = commandClass.getDeclaredAnnotation(Command.class);
                String[] names = command.names();
                if(names.length >= 1) {
                    name = names[0];
                    if(names.length >= 2)
                        aliases = Arrays.copyOfRange(names, 1, names.length);
                    description = command.desc();
                } else throw new CommandParseException("Commands must have at least 1 value");
            } else throw new CommandParseException("Commands must have the Command annotation");
            for(Field field : commandClass.getDeclaredFields()) {
                OptionMapping flag = AnnotationParser.parseOption(field);
                if(flag != null) {
                    optionMappings.add(flag);
                }
            }
            for(Method method : commandClass.getDeclaredMethods()) {
                CommandMapping rootMapping = AnnotationParser.parseRootMethod(method);
                if(rootMapping != null) {
                    if(this.rootMapping == null) {
                        this.rootMapping = rootMapping;
                        continue;
                    } else throw new CommandParseException("Too many root methods: only one root method can exist");
                }

                CommandMapping subCommand = AnnotationParser.parseSubCommandMethod(method);
                if(subCommand != null) {
                    subCommandMappings.add(subCommand);
                    continue;
                }

                PermissionMethodMapping permission = AnnotationParser.parsePermission(method);
                if(permission != null) {
                    if(permission.getCheck().scope() == PermissionScope.ROOT) {
                        if(rootPermissionMapping == null) {
                            rootPermissionMapping = permission;
                        } else throw new CommandParseException("Commands can only have one root permission mapping");
                    } else {
                        permissionMappings.add(permission);
                    }
                }
            }

            if(rootMapping == null)
                throw new CommandParseException("Root method mapping must exist");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append(" ");
        if(aliases.length >= 1)
            builder.append(Arrays.toString(aliases));
        builder.append("\n");
        if(!description.isEmpty())
            builder.append(description);
        builder.append(getOptions().toString());

        return builder.toString();
    }
}
