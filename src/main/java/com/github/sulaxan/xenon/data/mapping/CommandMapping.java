package com.github.sulaxan.xenon.data.mapping;

import com.github.sulaxan.xenon.annotation.SubCommand;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

public class CommandMapping implements CommandMethodMapping {

    private Method method;
    private Class<?> commandSenderClass;
    private boolean includeArgs;

    @Setter
    private SubCommand subCommand;

    public CommandMapping(Method method, Class<?> commandSenderClass, boolean includeArgs) {
        this.method = method;
        this.commandSenderClass = commandSenderClass;
        this.includeArgs = includeArgs;
    }

    public CommandMapping(Method method, Class<?> commandSenderClass, boolean includeArgs, SubCommand subCommand) {
        this.method = method;
        this.commandSenderClass = commandSenderClass;
        this.includeArgs = includeArgs;
        this.subCommand = subCommand;
    }

    @Override
    public Class<?> getCommandSenderClass() {
        return commandSenderClass;
    }

    @Override
    public boolean includeArgs() {
        return includeArgs;
    }

    @Override
    public boolean isSubCommand() {
        return subCommand != null;
    }

    @Override
    public SubCommand getSubCommand() {
        return subCommand;
    }

    @Override
    public Method getMethod() {
        return method;
    }
}
