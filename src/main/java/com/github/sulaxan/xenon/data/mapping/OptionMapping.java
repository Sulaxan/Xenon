package com.github.sulaxan.xenon.data.mapping;

import lombok.Getter;
import org.apache.commons.cli.Option;

import java.lang.reflect.Field;

@Getter
public class OptionMapping implements FieldMapping {

    private Field field;
    private Object defaultValue;
    private Option option;

    public OptionMapping(Field field, Object defaultValue, Option option) {
        this.field = field;
        this.defaultValue = defaultValue;
        this.option = option;
    }
}
