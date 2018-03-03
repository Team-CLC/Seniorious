package com.github.teamclc.seniorious.api.data.property.dummy;

import com.github.teamclc.seniorious.api.data.property.PropertyKey;
import com.github.teamclc.seniorious.api.data.property.UniquePropertyContainer;

import java.util.*;

@SuppressWarnings("unchecked")
public class DummyUniquePropertyContainer implements UniquePropertyContainer {
    private Map<PropertyKey, Object> propertyMap = new HashMap<>();

    @Override
    public <T> Optional<T> getProperty(PropertyKey<T> key) {
        return Optional.ofNullable((T) propertyMap.get(key));
    }

    @Override
    public <T> Optional<T> remove(PropertyKey<T> key) {
        return Optional.ofNullable((T) propertyMap.remove(key));
    }

    @Override
    public <T> Optional<T> addProperty(PropertyKey<T> key, T value) {
        return Optional.ofNullable((T) propertyMap.put(key, value));
    }

    @Override
    public Iterator<List<?>> iterator() {
        ArrayList<List<?>> list = new ArrayList<>();
        propertyMap.values().stream().map(Collections::singletonList).forEach(list::add);
        return list.iterator();
    }
}
