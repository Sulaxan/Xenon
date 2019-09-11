package com.github.sulaxan.xenon.data;

import com.github.sulaxan.xenon.data.mapping.*;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

@Getter
public class DefaultCommandData implements CommandData {

    private Class<?> commandClass;
    private Callable<Object[]> constructorArgs;
    private CommandMapping rootMapping;

    private List<FlagMapping> flagMappings = Lists.newArrayList();
    private List<ArgMapping> argMappings = Lists.newArrayList();

    private List<CommandMapping> subCommandMappings = Lists.newArrayList();
    private List<PermissionMapping> permissionMappings = Lists.newArrayList();

    public DefaultCommandData(Class<?> commandClass, Callable<Object[]> constructorArgs) {
        this.commandClass = commandClass;
        this.constructorArgs = constructorArgs;
    }

    public void parse() {
        try {
            for(Field field : commandClass.getDeclaredFields()) {
                FlagMapping flag = AnnotationParser.parseFlag(field);
                if(flag != null) {
                    flagMappings.add(flag);
                    continue;
                }

                ArgMapping arg = AnnotationParser.parseArg(field);
                if(arg != null) {
                    argMappings.add(arg);
                }
            }
            for(Method method : commandClass.getDeclaredMethods()) {
                this.rootMapping = AnnotationParser.parseRootMethod(method);

                CommandMapping subCommand = AnnotationParser.parseSubCommandMethod(method);
                if(subCommand != null) {
                    subCommandMappings.add(subCommand);
                    continue;
                }

                PermissionMapping permission = AnnotationParser.parsePermission(method);
                if(permission != null) {
                    permissionMappings.add(permission);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
