package com.github.sulaxan.xenon.data.mapping;

import java.lang.reflect.Field;

public interface FieldMapping {

    Field getField();

    Object getDefaultValue();
}
