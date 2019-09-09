package com.github.sulaxan.xenon.data;

import com.github.sulaxan.xenon.data.mapping.FieldMapping;
import com.github.sulaxan.xenon.data.mapping.MethodMapping;

import java.util.List;

public interface CommandData {

    Class<?> getCommandClass();

    MethodMapping getRootMapping();

    List<MethodMapping> getSubCommandMappings();

    List<MethodMapping> getPermissionMappings();

    List<FieldMapping> getFlagMappings();

    List<FieldMapping> getArgMappings();
}
