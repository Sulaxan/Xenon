package com.github.sulaxan.xenon.data.mapping;

import com.github.sulaxan.xenon.annotation.SubCommand;

public interface CommandMethodMapping extends MethodMapping {

    boolean includeArgs();

    boolean isSubCommand();

    SubCommand getSubCommand();
}
