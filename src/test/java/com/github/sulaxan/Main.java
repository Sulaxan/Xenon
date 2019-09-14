package com.github.sulaxan;

import com.github.sulaxan.xenon.sender.ConsoleCommandSender;
import com.github.sulaxan.xenon.internal.GenericCommandManager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GenericCommandManager manager = new GenericCommandManager();
        manager.register(TestCommand.class).build();

        Scanner s = new Scanner(System.in);

        while(true) {
            String line = s.nextLine();
            manager.execute(ConsoleCommandSender.INSTANCE, line);
        }
    }
}
