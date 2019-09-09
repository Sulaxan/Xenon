package com.github.sulaxan.xenon.data.mapping;

import lombok.Getter;

import java.lang.reflect.Field;

@Getter
public class ArgMapping implements FieldMapping {

    private Field field;
    private int index;
    private Object defaultValue;

    public ArgMapping(Field field, int index, Object defaultValue) {
        this.field = field;
        this.index = index;
        this.defaultValue = defaultValue;
    }
}
