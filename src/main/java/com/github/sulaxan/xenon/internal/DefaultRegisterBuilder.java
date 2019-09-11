package com.github.sulaxan.xenon.internal;

import com.github.sulaxan.xenon.data.DefaultCommandData;
import com.github.sulaxan.xenon.manager.RegisterBuilder;

import java.util.concurrent.Callable;

public class DefaultRegisterBuilder implements RegisterBuilder {

    private Class<?> commandClass;
    private Callable<Object[]> constructorArgs;

    @Override
    public RegisterBuilder withCommandClass(Class<?> commandClass) {
        this.commandClass = commandClass;
        return this;
    }

    @Override
    public RegisterBuilder withConstructorArgs(Object... args) {
        this.constructorArgs = () -> args;
        return this;
    }

    @Override
    public RegisterBuilder withConstructorArgs(Callable<Object[]> constructorArgs) {
        this.constructorArgs = constructorArgs;
        return this;
    }

    @Override
    public DefaultCommandData build() {
        return new DefaultCommandData(commandClass, constructorArgs);
    }
}
