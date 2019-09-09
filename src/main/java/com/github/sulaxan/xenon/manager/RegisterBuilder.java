package com.github.sulaxan.xenon.manager;

import com.github.sulaxan.xenon.data.DefaultCommandData;

import java.util.concurrent.Callable;

public interface RegisterBuilder {

    RegisterBuilder withCommandClass(Class<?> commandClass);

    RegisterBuilder withConstructorArgs(Object... args);

    RegisterBuilder withConstructorArgs(Callable<Object[]> callableArgs);

    DefaultCommandData build();
}
