package com.github.teamclc.seniorious;

import cc.huajistudio.aeb.EventBus;

/**
 * The main class which contains core features of this application.
 */
public class Seniorious {
    private static final EventBus EVENT_BUS = new EventBus();

    public static EventBus getEventBus() {
        return EVENT_BUS;
    }
}
