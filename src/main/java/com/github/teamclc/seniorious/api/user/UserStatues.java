package com.github.teamclc.seniorious.api.user;

public enum UserStatues {
    ONLINE(0x0A),
    Q_ME_PLEASE(0x3C),
    AFK(0x1E),
    BUSY(0x32),
    DO_NOT_DISTURB(0x46),
    INVISIBLE(0x28);

    private final byte id;

    UserStatues(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return id;
    }
}