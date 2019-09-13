package com.github.sulaxan.xenon.data;

import com.github.sulaxan.xenon.annotation.Command;
import com.github.sulaxan.xenon.data.mapping.*;
import com.github.sulaxan.xenon.exception.CommandParseException;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.cli.Options;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

@Getter
public class DefaultCommandData implements CommandData {

    private Class<?> commandClass;
    private String name;
    private String[] aliases = new String[0];
    private String description;
    private Callable<Object[]> constructorArgs;
    private CommandMapping rootMapping;

    private List<OptionMapping> optionMappings = Lists.newArrayList();

    private List<CommandMapping> subCommandMappings = Lists.newArrayList();
    private List<PermissionMapping> permissionMappings = Lists.newArrayList();

    public DefaultCommandData(Class<?> commandClass, Callable<Object[]> constructorArgs) {
        this.commandClass = commandClass;
        this.constructorArgs = constructorArgs;
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
    public Options getOptions() {
        Options options = new Options();
        for(OptionMapping mapping : optionMappings) {
            options.addOption(mapping.getOption());
        }
        return options;
    }

    @Override
    public CommandMethodMapping getSubCommand(String subCommand) {
        return null;
    }

    @Override
    public MethodMapping getSubCommandMapping(String subCommand) {
        return null;
    }

    @Override
    public CommandMapping getRootPermissionMapping() {
        return rootMapping;
    }

    @Override
    public PermissionMapping getPermissionMapping(String command) {
        for(PermissionMapping mapping : permissionMappings) {
            for(String sc : mapping.getCheck().subCommands()) {
                if(sc.equalsIgnoreCase(command))
                    return mapping;
            }
        }
        return null;
    }

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
                } else throw new CommandParseException("Commands must have at least 1 name");
            } else throw new CommandParseException("Commands must have the Command annotation");
            for(Field field : commandClass.getDeclaredFields()) {
                OptionMapping flag = AnnotationParser.parseFlag(field);
                if(flag != null) {
                    optionMappings.add(flag);
                }
            }
            for(Method method : commandClass.getDeclaredMethods()) {
                this.rootMapping = AnnotationParser.parseRootMethod(method);

                CommandMapping subCommand = AnnotationParser.parseSubCommandMethod(method);
                if(subCommand != null) {
                    subCommandMappings.add(subCommand);
                    continue;
                }

                PermissionMapping permission = AnnotationParser.parsePermission(method);
                if(permission != null) {
                    permissionMappings.add(permission);
                }
            }
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
