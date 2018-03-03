package com.github.teamclc.seniorious.api.data.property;

import java.util.Optional;

/**
 * Unique property container, all property key in it is unique.
 */
public interface UniquePropertyContainer extends PropertyContainer {
    <T> Optional<T> getProperty(PropertyKey<T> key);
    <T> Optional<T> remove(PropertyKey<T> key);

    @Override
    default <T> Optional<T> first(PropertyKey<T> key) {
        return getProperty(key);
    }

    @Override
    default <T> Optional<T> last(PropertyKey<T> key) {
        return getProperty(key);
    }

    @Override
    default <T> Optional<T> removeFirst(PropertyKey<T> key) {
        return remove(key);
    }

    @Override
    default <T> Optional<T> removeLast(PropertyKey<T> key) {
        return remove(key);
    }
}
