package com.github.sulaxan;

import com.github.sulaxan.xenon.annotation.Command;
import com.github.sulaxan.xenon.annotation.Option;
import com.github.sulaxan.xenon.annotation.Root;
import com.github.sulaxan.xenon.annotation.SubCommand;
import com.github.sulaxan.xenon.annotation.permission.PermissionCheck;
import com.github.sulaxan.xenon.annotation.permission.PermissionScope;
import com.github.sulaxan.xenon.sender.CommandSender;
import com.github.sulaxan.xenon.sender.ConsoleCommandSender;

@Command(names = {"test"}, desc = "Tests basic functionality of Xenon")
public class TestCommand {

    @Option(value = "p", longOption = "print", required = false, desc = "This value prints something")
    private boolean print;
    @Option(value = "e", desc = "Some other option that determines whether to exit the program")
    private boolean exit;
    @Option(value = "data", desc = "A data option")
    private String data;

    @Root
    public void root(CommandSender sender, String[] args) {
        sender.sendMessage("Print: " + print);
        sender.sendMessage("exit: " + exit);
        sender.sendMessage("data: " + data);
    }

    @SubCommand("subcommand")
    public void aSubCommand(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage("hi!");
    }

    @PermissionCheck(scope = PermissionScope.ROOT)
    public boolean hasPermission(ConsoleCommandSender sender) {
        return true;
    }

    @PermissionCheck(subCommands = {"subcommand"})
    public boolean hasPermissionSubCommand(ConsoleCommandSender sender) {
        return true;
    }
}
