package com.github.sulaxan;

import com.github.sulaxan.xenon.sender.ConsoleCommandSender;
import com.github.sulaxan.xenon.manager.DefaultCommandManager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DefaultCommandManager manager = new DefaultCommandManager();
        manager.register(TestCommand.class).build();

        DefaultCommandManager.enableHelp(manager);

        Scanner s = new Scanner(System.in);

        while(true) {
            String line = s.nextLine();
            manager.execute(ConsoleCommandSender.INSTANCE, line);
        }
    }
}
