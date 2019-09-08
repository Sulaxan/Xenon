package com.github.sulaxan.xenon.data;

import com.github.sulaxan.xenon.annotation.Root;
import com.github.sulaxan.xenon.data.mapping.FlagMapping;
import com.github.sulaxan.xenon.data.mapping.MethodMapping;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@Getter
public class CommandData {

    private Class<?> commandClass;
    private List<FlagMapping> fieldMappings = Lists.newCopyOnWriteArrayList();
    private List<MethodMapping> methodMappings = Lists.newCopyOnWriteArrayList();

    // Internal caching
    private Cache<String, Object> objectCache = CacheBuilder.newBuilder().build();

    public CommandData(Class<?> commandClass) {
        this.commandClass = commandClass;
    }

    public MethodMapping getRootMapping() {
        try {
            return (MethodMapping) objectCache.get("root_mapping", () -> {
                for(MethodMapping mapping : methodMappings) {
                    if(mapping.hasAnnotation(Root.class))
                        return mapping;
                }
                return null;
            });
        } catch (Exception e) {
            return null;
        }
    }

    public void parse() {
        try {
            for(Field field : commandClass.getDeclaredFields()) {
                Annotation[] annotations = field.getDeclaredAnnotations();
                if(annotations.length >= 1) {
                    fieldMappings.add(new FlagMapping(field, Lists.newArrayList(annotations)));
                }
            }

            for(Method method : commandClass.getDeclaredMethods()) {
                Annotation[] annotations = method.getDeclaredAnnotations();
                if(annotations.length >= 1) {
                    methodMappings.add(new MethodMapping(method, Lists.newArrayList(annotations)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
