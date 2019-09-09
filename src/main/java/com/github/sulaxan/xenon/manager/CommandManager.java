package com.github.sulaxan.xenon.manager;

import com.github.sulaxan.xenon.data.CommandData;
import com.github.sulaxan.xenon.sender.CommandSender;

import java.util.List;

public abstract class CommandManager {

    public abstract RegisterBuilder register(Class<?> commandClass);

    public abstract List<CommandData> getCommandData();

    public abstract CommandData findCommand(String command);

    public abstract void execute(CommandSender sender, String command);
}
