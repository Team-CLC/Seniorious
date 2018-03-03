package com.github.teamclc.seniorious.api.data.property;

import java.util.List;
import java.util.Optional;

public interface PropertyContainer extends Iterable<List<?>> {
    <T> Optional<T> first(PropertyKey<T> key);
    <T> Optional<T> last(PropertyKey<T> key);

    <T> Optional<T> addProperty(PropertyKey<T> key, T value);

    <T> Optional<T> removeFirst(PropertyKey<T> key);
    <T> Optional<T> removeLast(PropertyKey<T> key);
}
