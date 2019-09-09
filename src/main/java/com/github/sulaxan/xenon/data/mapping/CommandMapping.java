package com.github.sulaxan.xenon.data.mapping;

import com.github.sulaxan.xenon.annotation.SubCommand;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
public class CommandMapping implements MethodMapping {

    private Method method;
    private boolean includeArgs;

    @Setter
    private SubCommand subCommand;

    public CommandMapping(Method method, boolean includeArgs) {
        this.method = method;
        this.includeArgs = includeArgs;
    }

    public CommandMapping(Method method, boolean includeArgs, SubCommand subCommand) {
        this.method = method;
        this.includeArgs = includeArgs;
        this.subCommand = subCommand;
    }

    public boolean isSubCommand() {
        return subCommand != null;
    }
}
