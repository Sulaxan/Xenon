package com.github.sulaxan;

import com.github.sulaxan.xenon.internal.ConsoleCommandSender;
import com.github.sulaxan.xenon.internal.GenericCommandManager;

public class Main {

    public static void main(String[] args) {
        GenericCommandManager manager = new GenericCommandManager();
        manager.register(TestCommand.class).build();

        manager.execute(new ConsoleCommandSender(), "test -print 1");
    }
}
