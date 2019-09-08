package com.github.sulaxan.xenon.data.mapping;

import lombok.Getter;

import java.lang.reflect.Field;

@Getter
public class ArgMapping implements FieldMapping {

    private Field field;
    private Object defaultValue;

    public ArgMapping(Field field, Object defaultValue) {
        this.field = field;
        this.defaultValue = defaultValue;
    }
}
