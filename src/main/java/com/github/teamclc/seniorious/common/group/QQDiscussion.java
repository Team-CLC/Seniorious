package com.github.teamclc.seniorious.common.group;

import com.github.teamclc.seniorious.api.data.message.Message;
import com.github.teamclc.seniorious.api.lobby.Discussion;

public class QQDiscussion extends DummyLobby implements Discussion {
    public QQDiscussion(int id) {
        super(id);
    }

    @Override
    public void sendMessage(Message message) {

    }

    @Override
    public void sendMessage(Message... messages) {

    }
}
