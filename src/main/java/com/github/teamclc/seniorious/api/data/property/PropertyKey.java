package com.github.teamclc.seniorious.api.data.property;

import com.github.teamclc.seniorious.api.data.registry.RegistryEntry;

public interface PropertyKey<T> extends RegistryEntry<PropertyKey<T>> {
    Class<T> getType();

    default boolean isAssignableFrom(Class<?> clz) {
        return getType().isAssignableFrom(clz);
    }
}
