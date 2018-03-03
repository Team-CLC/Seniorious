package com.github.teamclc.seniorious.api.data.property.dummy;

import com.github.teamclc.seniorious.api.data.property.PropertyContainer;
import com.github.teamclc.seniorious.api.data.property.PropertyKey;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("unchecked")
public class DummyPropertyContainer implements PropertyContainer {
    private Map<PropertyKey<?>, List<?>> propertyMap = new HashMap<>();

    @Override
    public <T> Optional<T> first(PropertyKey<T> key) {
        return propertyMap.get(key).stream().findFirst().map(o -> (T) o);
    }

    @Override
    public <T> Optional<T> last(PropertyKey<T> key) {
        return propertyMap.get(key).stream().skip(propertyMap.get(key).size() - 1).findFirst().map(o -> (T) o);
    }

    @Override
    public <T> Optional<T> addProperty(PropertyKey<T> key, T value) {
        if (propertyMap.containsKey(key)) {
            ((List<T>) propertyMap.get(key)).add(value);
        } else {
            propertyMap.put(key, new ArrayList<>(Collections.singleton(value)));
        }
        return Optional.ofNullable(value);
    }

    @Override
    public <T> Optional<T> removeFirst(PropertyKey<T> key) {
        if (propertyMap.containsKey(key) && !propertyMap.get(key).isEmpty())
            return Optional.ofNullable((T) propertyMap.get(key).remove(0));
        else
            return Optional.empty();
    }

    @Override
    public <T> Optional<T> removeLast(PropertyKey<T> key) {
        if (propertyMap.containsKey(key) && !propertyMap.get(key).isEmpty())
            return Optional.ofNullable((T) propertyMap.get(key).remove(propertyMap.size() - 1));
        else
            return Optional.empty();
    }

    @NotNull
    @Override
    public Iterator<List<?>> iterator() {
        return propertyMap.values().iterator();
    }
}
