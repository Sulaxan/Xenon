package com.github.sulaxan.xenon.data.mapping;

import com.github.sulaxan.xenon.annotation.Flag;
import lombok.Getter;

import java.lang.reflect.Field;

@Getter
public class FlagMapping implements FieldMapping {

    private Field field;
    private Flag flag;
    private Object defaultValue;
    private boolean setIfExists;
    private boolean stopIfNotExist;

    public FlagMapping(Field field, Flag flag, Object defaultValue, boolean setIfExists, boolean stopIfNotExist) {
        this.field = field;
        this.flag = flag;
        this.defaultValue = defaultValue;
        this.setIfExists = setIfExists;
        this.stopIfNotExist = stopIfNotExist;
    }
}
