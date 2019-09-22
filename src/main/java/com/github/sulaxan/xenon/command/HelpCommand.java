package com.github.sulaxan.xenon.command;

import com.github.sulaxan.xenon.annotation.Command;
import com.github.sulaxan.xenon.annotation.Root;
import com.github.sulaxan.xenon.data.CommandData;
import com.github.sulaxan.xenon.data.mapping.CommandMethodMapping;
import com.github.sulaxan.xenon.data.mapping.OptionMapping;
import com.github.sulaxan.xenon.data.mapping.PermissionMethodMapping;
import com.github.sulaxan.xenon.manager.CommandManager;
import com.github.sulaxan.xenon.sender.CommandSender;
import com.google.common.collect.Lists;
import org.apache.commons.cli.Option;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Default command implementation for help.
 */
@Command(names = {"help"}, desc = "Get help information")
public class HelpCommand {

    private CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Root
    public void root(CommandSender sender, String[] args) {
        if(args.length >= 1) {
            CommandData data = manager.findCommand(args[0]);
            if(data != null) {
                sender.sendMessage(StringUtils.repeat('-', 25));
                sender.sendMessage("Help Information for '" + data.getName() + "'");
                sender.sendMessage("\t" + data.getDescription());
                if(data.getOptionMappings().size() >= 1) {
                    sender.sendMessage(" ");
                    sender.sendMessage("OPTIONS:");
                    for(OptionMapping mapping : data.getOptionMappings()) {
                        Option option = mapping.getOption();
                        List<String> options = Lists.newArrayListWithCapacity(2);
                        options.add("-" + option.getOpt());
                        if(option.hasLongOpt() && !option.getLongOpt().isEmpty())
                            options.add("--" +option.getLongOpt());
                        sender.sendMessage(String.format("\t%-25s %10s",
                                Arrays.toString(options.toArray()), option.getDescription()));
                    }
                }
                if(data.getSubCommandMappings().size() >= 1) {
                    sender.sendMessage(" ");
                    sender.sendMessage("SUB-COMMANDS:");
                    for(CommandMethodMapping mapping : data.getSubCommandMappings()) {
                        sender.sendMessage(String.format("\t%s", mapping.getSubCommand().value()));
                    }
                }

                sender.sendMessage(" ");
                sender.sendMessage("PERMISSIONS");
                if(data.getRootPermissionMapping() != null || data.getPermissionMappings().size() >= 1) {
                    if(data.getPermissionMappings() != null)
                        sender.sendMessage("\t[!] Root Permission Check");

                    if(data.getPermissionMappings() != null) {
                        for (PermissionMethodMapping mapping : data.getPermissionMappings()) {
                            sender.sendMessage("\t[!] Sub-Command Permission Check: " +
                                    Arrays.toString(mapping.getCheck().subCommands()));
                        }
                    }
                } else {
                    sender.sendMessage("\tNONE");
                }
                sender.sendMessage(StringUtils.repeat('-', 25));
            } else {
                sender.sendError("Could not find command " + args[0]);
            }
        } else {
            for (CommandData data : manager.getCommandData()) {
                sender.sendMessage(String.format("%s       %s", data.getName(), data.getDescription()));
            }
        }
    }
}
