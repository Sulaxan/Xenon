package com.github.sulaxan.xenon.data;

import com.github.sulaxan.xenon.annotation.*;
import com.github.sulaxan.xenon.annotation.defaults.DefaultValue;
import com.github.sulaxan.xenon.annotation.permission.PermissionCheck;
import com.github.sulaxan.xenon.data.mapping.ArgMapping;
import com.github.sulaxan.xenon.data.mapping.FlagMapping;
import com.github.sulaxan.xenon.data.mapping.PermissionMapping;
import com.github.sulaxan.xenon.data.mapping.CommandMapping;
import com.github.sulaxan.xenon.exception.CommandParseException;
import com.github.sulaxan.xenon.sender.CommandSender;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AnnotationParser {

    // Prevent initialization
    private AnnotationParser() {
    }

    public static FlagMapping parseFlag(Field field) {
        try {
            if(field.isAnnotationPresent(Flag.class)) {
                Object defaultValue = getDefaultValue(field);
                return new FlagMapping(
                        field,
                        field.getDeclaredAnnotation(Flag.class),
                        defaultValue,
                        field.isAnnotationPresent(SetIfExists.class),
                        field.isAnnotationPresent(StopIfNotExist.class)
                );
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArgMapping parseArg(Field field) {
        try {
            if(field.isAnnotationPresent(Param.class)) {
                Param param = field.getDeclaredAnnotation(Param.class);
                if(param.index() >= 0) {
                    return new ArgMapping(
                            field,
                            param.index(),
                            getDefaultValue(field),
                            field.isAnnotationPresent(StopIfNotExist.class)
                    );
                } else throw new IndexOutOfBoundsException("Param index must be greater than 0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static CommandMapping parseCommandType(Method method) {
        if(method.getParameterCount() >= 1 && method.getParameterCount() <= 2) {
            if(isValidParameter(method, 0, CommandSender.class)) {
                if(method.getParameterCount() == 2 && isValidParameter(method, 1, String[].class))
                    throw new CommandParseException("Second argument must either be excluded " +
                            "or be of type String[]");

                return new CommandMapping(method, method.getParameterCount() == 2);
            } else throw new CommandParseException("First argument must inherit CommandSender");
        } else throw new CommandParseException("Not enough parameters for method " +
                "(" + method.getName() + ")");
    }

    public static CommandMapping parseRootMethod(Method method) {
        if(method.isAnnotationPresent(Root.class)) {
            return parseCommandType(method);
        }

        return null;
    }

    public static CommandMapping parseSubCommandMethod(Method method) {
        if(method.isAnnotationPresent(SubCommand.class)) {
            CommandMapping mapping = parseCommandType(method);
            mapping.setSubCommand(method.getDeclaredAnnotation(SubCommand.class));

            return mapping;
        }

        return null;
    }

    public static PermissionMapping parsePermission(Method method) {
        if(method.isAnnotationPresent(PermissionCheck.class)) {
            if(method.getParameterCount() == 1) {
                if(isValidParameter(method, 0, CommandSender.class)) {
                    return new PermissionMapping(
                            method,
                            method.getDeclaredAnnotation(PermissionCheck.class)
                    );
                } else throw new ClassCastException("First parameter for permission mappings " +
                        "must inherit CommandSender");
            }
        }

        return null;
    }

    public static Object getDefaultValue(Field field) {
        try {
            for(Annotation annotation : field.getAnnotations()) {
                if(annotation.annotationType().isAnnotationPresent(DefaultValue.class)) {
                    // Temp
                    return annotation.annotationType().getDeclaredMethod("value").invoke(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isValidParameter(Method method, int parameterIndex, Class<?> clazz) {
        try {
            method.getParameterTypes()[parameterIndex].asSubclass(clazz);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
