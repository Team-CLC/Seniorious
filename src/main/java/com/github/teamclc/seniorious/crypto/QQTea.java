package com.github.teamclc.seniorious.crypto;

import com.github.teamclc.seniorious.JavaUtils;

public class QQTea {
    private static final int ROUNDS = 16;
    private static final int TEA_DELTA = 0x9e3779b9;
    private static final int TEA_DELTA_2 = 0xe3779b90;
    private TeaKey key;

    private byte[] ivOrLastBlock, current, cbcResult, decryptTempV;
    private boolean isFirstBlock;
    private int originalPtr;
    private int cbcPtr, cbcPtr2;

    public QQTea(TeaKey key) {
        this.key = key;
    }

    public byte[] encrypt(byte[] input) {
        ivOrLastBlock = new byte[8];
        current = new byte[8];
        isFirstBlock = true;
        cbcPtr = cbcPtr2 = 0;

        int inputLen = input.length;
        originalPtr = (input.length + 10) % 8;
        if (originalPtr != 0) originalPtr = 8 - originalPtr;

        cbcResult = new byte[inputLen + originalPtr + 10];
        current[0] = (byte) (((JavaUtils.getRandomNumber() & 0xF8) | originalPtr) & 0xFF);

        for (int i = 1; i < originalPtr; i++) current[i] = (byte) (JavaUtils.getRandomNumber() & 0xFF);
        originalPtr++;
        int temp = 1;
        while (temp <= 2) {
            if (originalPtr < 8) {
                current[originalPtr++] = (byte) (JavaUtils.getRandomNumber() & 0xFF);
                temp++;
            } else if (originalPtr == 8)
                calculateAndCBC();
        }
        temp = 0;
        while (inputLen > 0) {
            if (originalPtr < 8) {
                current[originalPtr++] = input[temp++];
                inputLen--;
            } else if (originalPtr == 8)
                calculateAndCBC();
        }
        temp = 1;
        while (temp <= 7) {
            if (originalPtr < 8) {
                current[originalPtr++] = (byte) (JavaUtils.getRandomNumber() & 0xFF);
                temp++;
            } else if (originalPtr == 8)
                calculateAndCBC();
        }

        return cbcResult;
    }

    public byte[] decrypt(byte[] input) {
        int temp = 0;
        byte[] tempBuf = new byte[8];

        int inputLen = input.length;
        if (inputLen % 8 != 0 || inputLen < 16)
            throw new IllegalArgumentException("bad input length");
        decryptTempV = input;
        return cbcResult;
    }

    private void calculateAndCBC() {

    }

    public TeaKey getKey() {
        return key;
    }

    public void setKey(TeaKey key) {
        this.key = key;
    }

    private void teaEncryptGroup(byte[] input, int inputOffset, byte[] output, int outputOffset) {
        int low = JavaUtils.bytesToInt(input, inputOffset);
        int high = JavaUtils.bytesToInt(input, inputOffset + 4);
        int sum = 0;
        for (int i = 0;i < ROUNDS;i++) {
            sum += TEA_DELTA;
            low += ((high << 4) + key.key1) ^ (high + sum) ^ ((high >>> 5) + key.key2);
            high += ((low << 4) + key.key3) ^ (low + sum) ^ ((low >>> 5) + key.key4);
        }
        JavaUtils.writeIntToBytes(low, output, 0);
        JavaUtils.writeIntToBytes(high, output, 4);
    }

    private void teaDecryptGroup(byte[] input, int inputOffset, byte[] output, int outputOffset) {
        int low = JavaUtils.bytesToInt(input, inputOffset);
        int high = JavaUtils.bytesToInt(input, inputOffset + 4);
        int sum = TEA_DELTA_2;
        for (int i = 0;i < ROUNDS;i++) {
            high -= ((low << 4) + key.key3) ^ (low + sum) ^ ((low >>> 5) + key.key4);
            low -= ((high << 4) + key.key1) ^ (high + sum) ^ ((high >>> 5) + key.key2);
            sum -= TEA_DELTA;
        }
        JavaUtils.writeIntToBytes(low, output, 0);
        JavaUtils.writeIntToBytes(high, output, 4);
    }
}
