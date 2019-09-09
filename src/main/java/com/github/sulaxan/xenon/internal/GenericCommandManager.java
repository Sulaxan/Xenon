package com.github.sulaxan.xenon.internal;

import com.github.sulaxan.xenon.data.DefaultCommandData;
import com.github.sulaxan.xenon.manager.CommandManager;
import com.github.sulaxan.xenon.manager.RegisterBuilder;
import com.github.sulaxan.xenon.sender.CommandSender;
import com.google.common.collect.Lists;

import java.util.List;

public class GenericCommandManager extends CommandManager {

    private List<DefaultCommandData> defaultCommandData = Lists.newCopyOnWriteArrayList();

    public GenericCommandManager() {
    }

    @Override
    public RegisterBuilder register(Class<?> commandClass) {
        return null;
    }

    @Override
    public List<DefaultCommandData> getCommandData() {
        return null;
    }

    @Override
    public DefaultCommandData findCommand(String command) {
        return null;
    }

    @Override
    public void execute(CommandSender sender, String command) {

    }
}
