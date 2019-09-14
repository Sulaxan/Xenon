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

    @Option(value = "print", required = true, desc = "Print")
    private boolean print;

    @Root
    public void root(CommandSender sender, String[] args) {
        if(print) {
            sender.sendMessage("output");
        } else {
            sender.sendError("No print!");
        }
    }

    @SubCommand(name = "subcommand")
    public void aSubCommand(CommandSender sender, String[] args) {
        sender.sendMessage("hi!");
    }

    @PermissionCheck(scope = PermissionScope.ROOT)
    public boolean hasPermission(CommandSender sender) {
        return true;
    }
}
