package com.github.teamclc.seniorious.common.group;

import com.github.teamclc.seniorious.api.data.message.Message;
import com.github.teamclc.seniorious.api.lobby.Group;

public class QQGroup extends DummyLobby implements Group {
    public QQGroup(int id) {
        super(id);
    }

    @Override
    public void sendMessage(Message message) {

    }

    @Override
    public void sendMessage(Message... messages) {

    }
}
