package com.github.teamclc.seniorious.common.group;

import com.github.teamclc.seniorious.api.data.identify.Identifier;
import com.github.teamclc.seniorious.api.data.property.PropertyContainer;
import com.github.teamclc.seniorious.api.data.property.dummy.DummyUniquePropertyContainer;
import com.github.teamclc.seniorious.api.lobby.Lobby;
import com.github.teamclc.seniorious.api.user.User;

import java.util.*;

public abstract class DummyLobby implements Lobby {
    private Map<Identifier, User> userMap = new HashMap<>();
    private final int id;

    public DummyLobby(int id) {
        this.id = id;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> getUser(Identifier identifier) {
        return Optional.empty();
    }

    @Override
    public void addUser(User user) {
        userMap.put(user.getIdentifer(), user);
    }

    @Override
    public User removeUser(Identifier identifier) {
        return userMap.remove(identifier);
    }

    @Override
    public void removeUser(User user) {
        userMap.remove(user.getIdentifer());
    }

    @Override
    public Identifier getIdentifer() {
        return Identifier.of(id);
    }

    @Override
    public PropertyContainer getPropertyContainer() {
        return new DummyUniquePropertyContainer();
    }
}
