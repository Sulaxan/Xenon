package com.github.sulaxan.xenon.data;

import com.github.sulaxan.xenon.annotation.Flag;
import com.github.sulaxan.xenon.annotation.Param;
import com.github.sulaxan.xenon.annotation.SetIfExists;
import com.github.sulaxan.xenon.annotation.StopIfNotExist;
import com.github.sulaxan.xenon.annotation.defaults.DefaultValue;
import com.github.sulaxan.xenon.annotation.permission.PermissionCheck;
import com.github.sulaxan.xenon.data.mapping.ArgMapping;
import com.github.sulaxan.xenon.data.mapping.FlagMapping;
import com.github.sulaxan.xenon.data.mapping.PermissionMapping;
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
                            getDefaultValue(field)
                    );
                } else throw new IndexOutOfBoundsException("Param index must be greater than 0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static PermissionMapping parsePermission(Method method) {
        if(method.isAnnotationPresent(PermissionCheck.class)) {
            if(method.getParameterCount() == 1) {
                try {
                    method.getParameterTypes()[0].asSubclass(CommandSender.class);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    System.err.println("Class must inherit CommandSender");
                    return null;
                }

                return new PermissionMapping(
                        method,
                        method.getDeclaredAnnotation(PermissionCheck.class)
                );
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
}
