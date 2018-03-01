package com.github.teamclc.seniorious.crypto;

import com.github.teamclc.luna.Utils;
import com.github.teamclc.seniorious.JavaUtils;

public class QQTea {
    private static final int ROUNDS = 16;
    private static final int TEA_DELTA = 0x9e3779b9;
    private static final int TEA_DELTA_2 = 0xe3779b90;
    private TeaKey key;

    private byte[] ivOrLastBlock, current, cbcResult, decryptTempV, cbcTemp;
    private boolean isFirstBlock;
    private int originalPtr;
    private int cbcPtr, cbcPtr2;

    public QQTea(TeaKey key) {
        this.key = key;
    }

    public byte[] encrypt(byte[] input) {
        ivOrLastBlock = new byte[8];
        cbcTemp = new byte[8];
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
        byte[] tempBuf = new byte[8];
        ivOrLastBlock = new byte[8];

        int inputLen = input.length;
        if (inputLen % 8 != 0 || inputLen < 16)
            throw new IllegalArgumentException("bad input length");
        decryptTempV = input;
        teaDecryptGroup(input, ivOrLastBlock);

        originalPtr = ivOrLastBlock[0] & 0x07;
        int outputRemainingLen = inputLen - originalPtr - 10;
        if (outputRemainingLen < 0)
            throw new IllegalArgumentException("bad padding: negative!");

        cbcResult = new byte[outputRemainingLen];
        cbcPtr = 8;
        cbcPtr2 = 0;
        originalPtr++;
        int temp = 1;
        while (temp <= 2) {
            if (originalPtr < 8) {
                originalPtr++; temp++;
            } else if (originalPtr == 8) {
                tempBuf = input;
                if (decryptUnknown1())
                    throw new IllegalArgumentException();
            }
        }

        temp = 0;
        while (outputRemainingLen != 0) {
            if (originalPtr < 8) {
                cbcResult[temp] = (byte) ((tempBuf[cbcPtr2 + originalPtr] ^ ivOrLastBlock[originalPtr]) & 0xFF);
                temp++;
                outputRemainingLen--;
                originalPtr++;
            } else if (originalPtr == 8) {
                tempBuf = input;
                cbcPtr2 = cbcPtr - 8;
                if (decryptUnknown1())
                    throw new IllegalArgumentException();
            }
        }

        for (int i = 1; i < 8; i++) {
            if (originalPtr < 8) {
                if ((tempBuf[cbcPtr2 + originalPtr] ^ ivOrLastBlock[originalPtr]) != 0)
                    throw new IllegalArgumentException();
                originalPtr++;
            } else if (originalPtr == 8) {
                tempBuf = input;
                cbcPtr2 = cbcPtr;
                if (decryptUnknown1())
                    throw new IllegalArgumentException();
            }
        }
        return cbcResult;
    }

    private boolean decryptUnknown1() {
        for (int i = 0;i < 8;i++)
            ivOrLastBlock[i] ^= decryptTempV[cbcPtr + i];
        teaDecryptGroup(ivOrLastBlock, ivOrLastBlock);
        cbcPtr += 8;
        originalPtr = 0;
        return false;
    }

    private void calculateAndCBC() {
        for (int i = 0;i < 8;i++) {
            if (isFirstBlock)
                current[i] ^= ivOrLastBlock[i];
            else
                current[i] ^= cbcResult[cbcPtr2 + i];
        }
        teaEncryptGroup(current, cbcTemp);
        for (int i = 0;i < 8;i++) {
            cbcResult[cbcPtr + i] = (byte) ((cbcTemp[i] ^ ivOrLastBlock[i]) & 0xFF);
            ivOrLastBlock[i] = current[i];
        }
        cbcPtr2 = cbcPtr;
        cbcPtr += 8;
        originalPtr = 0;
        isFirstBlock = false;
    }

    public TeaKey getKey() {
        return key;
    }

    public void setKey(TeaKey key) {
        this.key = key;
    }

    private void teaEncryptGroup(byte[] input, byte[] output) {
        int low = JavaUtils.bytesToInt(input, 0);
        int high = JavaUtils.bytesToInt(input, 4);
        int sum = 0;
        for (int i = 0;i < ROUNDS;i++) {
            sum += TEA_DELTA;
            low += ((high << 4) + key.key1) ^ (high + sum) ^ ((high >>> 5) + key.key2);
            high += ((low << 4) + key.key3) ^ (low + sum) ^ ((low >>> 5) + key.key4);
        }
        JavaUtils.writeIntToBytes(low, output, 0);
        JavaUtils.writeIntToBytes(high, output, 4);
    }

    private void teaDecryptGroup(byte[] input, byte[] output) {
        int low = JavaUtils.bytesToInt(input, 0);
        int high = JavaUtils.bytesToInt(input, 4);
        int sum = TEA_DELTA_2;
        for (int i = 0;i < ROUNDS;i++) {
            high -= ((low << 4) + key.key3) ^ (low + sum) ^ ((low >>> 5) + key.key4);
            low -= ((high << 4) + key.key1) ^ (high + sum) ^ ((high >>> 5) + key.key2);
            sum -= TEA_DELTA;
        }
        JavaUtils.writeIntToBytes(low, output, 0);
        JavaUtils.writeIntToBytes(high, output, 4);
    }

    public static void main(String[] args) {
        QQTea tea = new QQTea(new TeaKey(new byte[] {
                (byte) 0xBA, 0x42, (byte) 0xFF, 0x01, (byte) 0xCF, (byte) 0xB4, (byte) 0xFF, (byte) 0xD2, 0x12, (byte) 0xF0, 0x6E, (byte) 0xA7, 0x1B, 0x7C, (byte) 0xB3, 0x08
        }));
        byte[] ret = tea.decrypt(new byte[] {
                0x16, (byte) 0xA3, 0x06, (byte) 0xCE, 0x2D, (byte) 0xBD, 0x2B, 0x72, (byte) 0xAB, 0x17, (byte) 0xFC, 0x7D, (byte) 0xE0, (byte) 0xFE, (byte) 0x89, (byte) 0xEA, (byte) 0xD1, 0x1F, 0x5D, (byte) 0x9B, 0x64, (byte) 0xEC, (byte) 0xCB, 0x69
        });
        System.out.println(ret.length);
        System.out.println(Utils.bytesToHexString(ret));
    }
}
