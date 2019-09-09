package com.github.sulaxan.xenon.data;

import com.github.sulaxan.xenon.annotation.Flag;
import com.github.sulaxan.xenon.annotation.SetIfExists;
import com.github.sulaxan.xenon.annotation.StopIfNotExist;
import com.github.sulaxan.xenon.annotation.defaults.DefaultValue;
import com.github.sulaxan.xenon.data.mapping.FlagMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

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
                        field.getAnnotation(Flag.class),
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

    public static Object getDefaultValue(Field field) {
        try {
            for(Annotation annotation : field.getAnnotations()) {
                if(annotation.annotationType().isAnnotationPresent(DefaultValue.class)) {
                    // Temp
                    return annotation.annotationType().getMethod("value").invoke(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
