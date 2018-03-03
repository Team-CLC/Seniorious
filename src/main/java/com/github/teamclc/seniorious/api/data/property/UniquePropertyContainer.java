package com.github.teamclc.seniorious.api.data.property;

/**
 * Unique property container, all property key in it is unique.
 */
public interface UniquePropertyContainer {
    <T> T getProperty(PropertyKey<T> key);
}
