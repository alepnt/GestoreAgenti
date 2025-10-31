package com.example.GestoreAgenti.service.crud;

import java.util.Arrays;

import org.springframework.beans.BeanUtils;

/**
 * Default handler that merges entities by copying every property from the
 * incoming instance into the managed one, optionally skipping some attributes
 * (for example identifiers).
 */
public class BeanCopyCrudEntityHandler<T> implements CrudEntityHandler<T> {

    private final String[] ignoredProperties;

    public BeanCopyCrudEntityHandler(String... ignoredProperties) {
        this.ignoredProperties = ignoredProperties == null ? new String[0] : ignoredProperties.clone();
    }

    @Override
    public T merge(T existing, T changes) {
        BeanUtils.copyProperties(changes, existing, ignoredProperties);
        return existing;
    }

    @Override
    public String toString() {
        return "BeanCopyCrudEntityHandler" + Arrays.toString(ignoredProperties);
    }
}
