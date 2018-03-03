package com.github.teamclc.seniorious.api.data.message;

/**
 * Sender which can send message.
 * For instance, a QQ group is a message sender.
 */
public interface MessageSender {
    void sendMessage(Message message);

    void sendMessage(Message... messages);
}
