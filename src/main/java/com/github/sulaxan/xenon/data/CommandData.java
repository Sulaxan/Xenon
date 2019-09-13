package com.github.sulaxan.xenon.data;

import com.github.sulaxan.xenon.data.mapping.CommandMethodMapping;
import com.github.sulaxan.xenon.data.mapping.MethodMapping;
import com.github.sulaxan.xenon.data.mapping.OptionMapping;
import org.apache.commons.cli.Options;

import java.util.List;
import java.util.concurrent.Callable;

public interface CommandData {

    Class<?> getCommandClass();

    Callable<Object[]> getConstructorArgs();

    String getName();

    String[] getAliases();

    String getDescription();

    List<OptionMapping> getOptionMappings();

    Options getOptions();

    CommandMethodMapping getRootMapping();

    List<CommandMethodMapping> getSubCommandMappings();

    CommandMethodMapping getSubCommand(String subCommand);

    List<MethodMapping> getPermissionMappings();

    MethodMapping getSubCommandMapping(String subCommand);

    MethodMapping getRootPermissionMapping();

    MethodMapping getPermissionMapping(String subCommand);
}
