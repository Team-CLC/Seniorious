package com.github.teamclc.seniorious.api.data.registry;

public interface RegistryEntry<V> {
    String getRegistryName();

    V setRegistryName(String registryName);
}
