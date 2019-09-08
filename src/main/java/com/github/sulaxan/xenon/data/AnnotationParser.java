package com.github.sulaxan.xenon.data;

import com.github.sulaxan.xenon.annotation.Flag;
import com.github.sulaxan.xenon.data.mapping.FlagMapping;

import java.lang.reflect.Field;

public class AnnotationParser {

    // Prevent initialization
    private AnnotationParser() {
    }

    public static FlagMapping parseFlag(Field field) {
        try {
            if(field.isAnnotationPresent(Flag.class)) {

            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
