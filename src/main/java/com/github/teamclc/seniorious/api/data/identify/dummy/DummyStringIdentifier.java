package com.github.teamclc.seniorious.api.data.identify.dummy;

import com.github.teamclc.seniorious.api.data.identify.Identifier;

import java.util.Objects;

public class DummyStringIdentifier implements Identifier {
    private final String id;

    public DummyStringIdentifier(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DummyStringIdentifier that = (DummyStringIdentifier) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DummyStringIdentifier{" +
                "id='" + id + '\'' +
                '}';
    }
}
