package me.sulaxan.xenon;

import me.sulaxan.xenon.sender.CommandSender;

public interface Command {

    void onCommand(CommandSender sender, String[] args);
}
