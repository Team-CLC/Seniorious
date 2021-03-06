package com.github.teamclc.seniorious.crypto;

import com.github.teamclc.seniorious.JavaUtils;

/**
 * QQTea 加密算法. <br />
 * Translated from JS <br />
 * @see <a href="http://blog.csdn.net/gsls200808/article/details/70837455">Refered blog</a>
 * @see <a href="http://imgcache.qq.com/ptlogin/ver/10131/js/c_login_2.js">Original Javascript</a>
 */
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
        originalPtr = (inputLen + 10) % 8;
        if (originalPtr != 0) originalPtr = 8 - originalPtr;

        cbcResult = new byte[inputLen + originalPtr + 10];
        current[0] = (byte) (((byte) (JavaUtils.getRandomNumber() & 0xF8 & 0xFF) | originalPtr) & 0xFF);

        for (int i = 1; i < originalPtr; i++) current[i] = (byte) (JavaUtils.getRandomNumber() & 0xFF);
        originalPtr++;
        int temp = 1;
        while (temp <= 2) {
            if (originalPtr < 8) {
                current[originalPtr++] = (byte) (JavaUtils.getRandomNumber() & 0xFF);
                temp++;
            }
            if (originalPtr == 8)
                calculateAndCBC();
        }
        temp = 0;
        while (inputLen > 0) {
            if (originalPtr < 8) {
                current[originalPtr++] = input[temp++];
                inputLen--;
            }
            if (originalPtr == 8)
                calculateAndCBC();
        }
        temp = 1;
        while (temp <= 7) {
            if (originalPtr < 8) {
                current[originalPtr++] = 0;
                temp++;
            }
            if (originalPtr == 8)
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
            }
            if (originalPtr == 8) {
                tempBuf = input;
                decryptUnknown1();
            }
        }

        temp = 0;
        while (outputRemainingLen != 0) {
            if (originalPtr < 8) {
                cbcResult[temp] = (byte) ((tempBuf[cbcPtr2 + originalPtr] ^ ivOrLastBlock[originalPtr]) & 0xFF);
                temp++;
                outputRemainingLen--;
                originalPtr++;
            }
            if (originalPtr == 8) {
                tempBuf = input;
                cbcPtr2 = cbcPtr - 8;
                decryptUnknown1();
            }
        }

        for (int i = 1; i < 8; i++) {
            if (originalPtr < 8) {
                if ((tempBuf[cbcPtr2 + originalPtr] ^ ivOrLastBlock[originalPtr]) != 0)
                    throw new IllegalArgumentException();
                originalPtr++;
            }
            if (originalPtr == 8) {
                tempBuf = input;
                cbcPtr2 = cbcPtr;
                decryptUnknown1();
            }
        }
        return cbcResult;
    }

    int cnt = 0;
    private void decryptUnknown1() {
        cnt++;
        int maxLen = Math.min(8, decryptTempV.length - cbcPtr);
        for (int i = 0;i < maxLen;i++)
            ivOrLastBlock[i] ^= decryptTempV[cbcPtr + i];
        teaDecryptGroup(ivOrLastBlock, ivOrLastBlock);
        cbcPtr += 8;
        originalPtr = 0;
    }

    private void calculateAndCBC() {
        for (int i = 0;i < 8;i++)
            if (isFirstBlock)
                current[i] ^= ivOrLastBlock[i];
            else
                current[i] ^= cbcResult[cbcPtr2 + i];
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
}
