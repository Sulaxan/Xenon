package com.github.sulaxan.xenon.data.mapping;

import java.lang.reflect.Field;

/**
 * Field mappings are used to indicate the properties of specific
 * fields in a command class.
 */
public interface FieldMapping {

    /**
     * @return The target field.
     */
    Field getField();

    /**
     * @return The default value of the field if none is specified by
     * the runtime parser.
     */
    Object getDefaultValue();
}
