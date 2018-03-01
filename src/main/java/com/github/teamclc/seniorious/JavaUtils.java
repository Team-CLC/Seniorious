package com.github.teamclc.seniorious;

public class JavaUtils {
    public static int bytesToInt(byte[] bytes, int offset) {
        int res = 0;
        if (offset + 4 >= bytes.length)
            throw new ArrayIndexOutOfBoundsException();
        for (int i = offset; i < offset + 4;i++) {
            res <<= 8;
            res |= bytes[i];
        }
        return res;
    }

    public static void writeIntToBytes(int num, byte[] bytes, int offset) {
        bytes[offset + 3] = (byte) (num & 0xFF);
        bytes[offset + 2] = (byte) ((num >> 8) & 0xFF);
        bytes[offset + 1] = (byte) ((num >> 16) & 0xFF);
        bytes[offset] = (byte) ((num >> 24) & 0xFF);
    }

    public static int getRandomNumber() {
        return (int) Math.round(Math.random() * 0xFFFFFFFF);
    }
}
