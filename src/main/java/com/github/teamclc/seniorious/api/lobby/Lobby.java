package com.github.teamclc.seniorious.api.lobby;

import com.github.teamclc.seniorious.api.data.identify.Identifiable;
import com.github.teamclc.seniorious.api.data.identify.Identifier;
import com.github.teamclc.seniorious.api.data.message.MessageSender;
import com.github.teamclc.seniorious.api.data.property.PropertyContainerEntry;
import com.github.teamclc.seniorious.api.user.User;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * A QQ chat lobby which contains various users in it.
 */
public interface Lobby extends MessageSender, Identifiable, Iterable<User>, PropertyContainerEntry {
    List<User> getUsers();

    Optional<User> getUser(Identifier identifier);

    void addUser(User user);

    User removeUser(Identifier identifier);
    void removeUser(User user);

    @Override
    default Iterator<User> iterator() {
        return getUsers().iterator();
    }
}
