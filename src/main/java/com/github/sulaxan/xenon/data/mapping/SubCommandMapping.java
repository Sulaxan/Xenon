package com.github.sulaxan.xenon.data.mapping;

import com.github.sulaxan.xenon.annotation.SubCommand;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class SubCommandMapping implements MethodMapping {

    private Method method;
    private SubCommand command;

    public SubCommandMapping(Method method, SubCommand command) {
        this.method = method;
        this.command = command;
    }
}
