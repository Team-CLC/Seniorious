package com.github.teamclc.seniorious.api.lobby;

import com.github.teamclc.seniorious.api.data.property.PropertyKey;
import com.github.teamclc.seniorious.api.data.property.dummy.DummyPropertyKey;

import java.time.Instant;

public interface LobbyProperties {
    PropertyKey<Instant> CREATE_TIME = new DummyPropertyKey<>(Instant.class, "lobby:create_time");
}
