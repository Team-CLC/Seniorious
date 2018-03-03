package com.github.teamclc.seniorious.api.data.identify;

import com.github.teamclc.seniorious.api.data.identify.dummy.DummyIntIdentifier;
import com.github.teamclc.seniorious.api.data.identify.dummy.DummyStringIdentifier;

public interface Identifier {
    static DummyIntIdentifier of(int id) {
        return new DummyIntIdentifier(id);
    }

    static DummyStringIdentifier of(String id) {
        return new DummyStringIdentifier(id);
    }
}
