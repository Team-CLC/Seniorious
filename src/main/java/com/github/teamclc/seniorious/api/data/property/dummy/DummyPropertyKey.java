package com.github.teamclc.seniorious.api.data.property.dummy;

import com.github.teamclc.seniorious.api.data.property.PropertyKey;

public class DummyPropertyKey<T> implements PropertyKey<T> {
    private final Class<T> type;
    private String registryName;

    public DummyPropertyKey(Class<T> type, String registryName) {
        if (type == null)
            throw new IllegalArgumentException("Class type cannot be null!");
        this.type = type;
    }

    @Override
    public String getRegistryName() {
        return registryName;
    }

    @Override
    public DummyPropertyKey<T> setRegistryName(String registryName) {
        this.registryName = registryName;
        return this;
    }

    @Override
    public Class<T> getType() {
        return type;
    }
}
