package com.github.teamclc.seniorious.api.data.message;

/**
 * Sendable message which is able to accept by QQ client.
 */
public interface Sendable {
    /**
     * Raw message to send
     */
    String getRawMessage();
}
