package com.github.teamclc.seniorious.crypto;

import com.github.teamclc.seniorious.JavaUtils;

import java.util.Objects;

public final class TeaKey {
    public int key1, key2, key3, key4;

    public TeaKey(byte[] bytes) {
        this(bytes, 0);
    }

    public TeaKey(byte[] bytes, int offset) {
        if (offset + 16 >= bytes.length)
            throw new ArrayIndexOutOfBoundsException();
        key1 = JavaUtils.bytesToInt(bytes, offset);
        key2 = JavaUtils.bytesToInt(bytes, offset + 4);
        key3 = JavaUtils.bytesToInt(bytes, offset + 8);
        key4 = JavaUtils.bytesToInt(bytes, offset + 12);
    }

    public TeaKey(int key1, int key2, int key3, int key4) {
        this.key1 = key1;
        this.key2 = key2;
        this.key3 = key3;
        this.key4 = key4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeaKey teaKey = (TeaKey) o;
        return key1 == teaKey.key1 &&
                key2 == teaKey.key2 &&
                key3 == teaKey.key3 &&
                key4 == teaKey.key4;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key1, key2, key3, key4);
    }
}
