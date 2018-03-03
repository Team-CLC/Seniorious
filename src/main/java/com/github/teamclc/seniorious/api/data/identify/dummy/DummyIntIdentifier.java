package com.github.teamclc.seniorious.api.data.identify.dummy;

import com.github.teamclc.seniorious.api.data.identify.Identifier;

import java.util.Objects;

public class DummyIntIdentifier implements Identifier {
    private final int id;

    public DummyIntIdentifier(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DummyIntIdentifier that = (DummyIntIdentifier) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DummyIntIdentifier{" +
                "id=" + id +
                '}';
    }
}
