package com.github.sulaxan;

import com.github.sulaxan.xenon.annotation.Command;
import com.github.sulaxan.xenon.annotation.Option;
import com.github.sulaxan.xenon.annotation.Root;
import com.github.sulaxan.xenon.annotation.SubCommand;
import com.github.sulaxan.xenon.annotation.permission.PermissionCheck;
import com.github.sulaxan.xenon.annotation.permission.PermissionScope;
import com.github.sulaxan.xenon.sender.CommandSender;

@Command(names = {"test"}, desc = "HI!")
public class TestCommand {

    @Option(value = "print", required = false, desc = "Print")
    private boolean print;
    @Option(value = "e", desc = "Quit")
    private boolean exit;
    @Option(value = "data", desc = "Data")
    private String data;

    @Root
    public void root(CommandSender sender, String[] args) {
        sender.sendMessage("Print: " + print);
        sender.sendMessage("exit: " + exit);
        sender.sendMessage("data: " + data);
    }

    @SubCommand(name = "subcommand")
    public void aSubCommand(CommandSender sender, String[] args) {
        sender.sendMessage("hi!");
    }

    @PermissionCheck(scope = PermissionScope.ROOT)
    public boolean hasPermission(CommandSender sender) {
        return true;
    }

    @PermissionCheck(subCommands = {"subcommand"})
    public boolean hasPermissionSubCommand(CommandSender sender) {
        return false;
    }
}
