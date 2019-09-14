package com.github.sulaxan.xenon.data;

import com.github.sulaxan.xenon.data.mapping.CommandMethodMapping;
import com.github.sulaxan.xenon.data.mapping.OptionMapping;
import com.github.sulaxan.xenon.data.mapping.PermissionMethodMapping;
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

    List<PermissionMethodMapping> getPermissionMappings();

    PermissionMethodMapping getRootPermissionMapping();

    PermissionMethodMapping getPermissionMapping(String subCommand);
}
