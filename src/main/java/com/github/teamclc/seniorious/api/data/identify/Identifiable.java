package com.github.teamclc.seniorious.api.data.identify;

/**
 * Identifiable classes.
 * For instance, QQ group has an unique identifier which is integer.
 */
public interface Identifiable {
    Identifier getIdentifer();

    /**
     * Rewrites equals with identifier.
     */
    boolean equals(Object o);
}
