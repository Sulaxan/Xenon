package com.github.sulaxan.xenon;

import com.github.sulaxan.xenon.sender.CommandSender;

public interface Command {

    void onCommand(CommandSender sender, String[] args);
}
