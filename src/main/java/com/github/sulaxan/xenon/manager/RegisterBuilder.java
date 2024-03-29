package com.github.sulaxan.xenon.manager;

import com.github.sulaxan.xenon.data.CommandData;

import java.util.concurrent.Callable;

public interface RegisterBuilder {

    RegisterBuilder withCommandClass(Class<?> commandClass);

    RegisterBuilder withConstructorArgs(Object... args);

    RegisterBuilder withConstructorArgs(Callable<Object[]> callableArgs);

    CommandData build();
}
