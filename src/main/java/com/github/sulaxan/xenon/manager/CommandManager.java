package com.github.sulaxan.xenon.manager;

import com.github.sulaxan.xenon.data.DefaultCommandData;
import com.github.sulaxan.xenon.sender.CommandSender;

import java.util.List;

public abstract class CommandManager {

    public abstract RegisterBuilder register(Class<?> commandClass);

    public abstract List<DefaultCommandData> getCommandData();

    public abstract DefaultCommandData findCommand(String command);

    public abstract void execute(CommandSender sender, String command);
}
