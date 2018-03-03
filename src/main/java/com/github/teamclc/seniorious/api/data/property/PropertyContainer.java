package com.github.teamclc.seniorious.api.data.property;

public interface PropertyContainer extends Iterable<Object> {
    <T> T first(PropertyKey<T> key);
    <T> T last(PropertyKey<T> key);

    <T> T addProperty(PropertyKey<T> key, T value);

    <T> T removeFirst(PropertyKey<T> key);
    <T> T removeLast(PropertyKey<T> key);
}
