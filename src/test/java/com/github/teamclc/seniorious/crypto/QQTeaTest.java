package com.github.teamclc.seniorious.crypto;

import com.github.teamclc.seniorious.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class QQTeaTest {
    private static final int MAX_DATA_LEN = 2048;
    private static final int TEST_ROUNDS = 24;
    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");

    @Before
    public void setUp() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/tea.js");
        byte[] temp = new byte[is.available()];
        int r = is.read(temp);
        if (r <= 0) throw new IOException();
        engine.eval(new String(temp));
    }

    @Test
    public void testDecryptFixedData() {
        QQTea tea = new QQTea(new TeaKey(new byte[] {
                (byte) 0xBA, 0x42, (byte) 0xFF, 0x01, (byte) 0xCF, (byte) 0xB4, (byte) 0xFF, (byte) 0xD2, 0x12, (byte) 0xF0, 0x6E, (byte) 0xA7, 0x1B, 0x7C, (byte) 0xB3, 0x08
        }));

        byte[] ret = tea.decrypt(new byte[] {
                0x16, (byte) 0xA3, 0x06, (byte) 0xCE, 0x2D, (byte) 0xBD, 0x2B, 0x72, (byte) 0xAB, 0x17, (byte) 0xFC, 0x7D, (byte) 0xE0, (byte) 0xFE, (byte) 0x89, (byte) 0xEA, (byte) 0xD1, 0x1F, 0x5D, (byte) 0x9B, 0x64, (byte) 0xEC, (byte) 0xCB, 0x69
        });

        byte[] excepted = new byte[] {
                0x7A, 0x68, 0x69, 0x72, 0x75, 0x69, 0x5F, 0x31, 0x32, 0x33, 0x00
        };
        Assert.assertArrayEquals("fixedData", excepted, ret);
    }

    @Test
    public void testEncryptAndDecrypt() {
        for (int i = 0;i < TEST_ROUNDS;i++) {
            Random random = new Random();

            byte[] key = new byte[16];
            random.nextBytes(key);

            int len = random.nextInt(MAX_DATA_LEN);
            byte[] originalData = new byte[len];
            random.nextBytes(originalData);

            QQTea tea = new QQTea(new TeaKey(key));
            byte[] encryptedResult = tea.encrypt(originalData);
            byte[] decryptedResult = tea.decrypt(encryptedResult);

            Assert.assertArrayEquals(originalData, decryptedResult);
        }
    }

    @Test
    public void testEncrypt() throws ScriptException {
        for (int i = 0;i < TEST_ROUNDS;i++) {
            Random random = new Random();

            byte[] key = new byte[16];
            random.nextBytes(key);

            int len = random.nextInt(MAX_DATA_LEN);
            byte[] originalData = new byte[len];
            random.nextBytes(originalData);

            QQTea tea = new QQTea(new TeaKey(key));
            byte[] encryptedData = tea.encrypt(originalData);

            String decryptedData = ((String) engine.eval("TEA.initkey('" + Utils.toHexString(key) + "');TEA.decrypt('" + Utils.toHexString(encryptedData) + "')"))
                    .toLowerCase();
            String originalString = Utils.toHexString(originalData);

            Assert.assertEquals(originalString, decryptedData);
        }
    }

    @Test
    public void testDecrypt() throws ScriptException {
        for (int i = 0;i < TEST_ROUNDS;i++) {
            Random random = new Random();

            byte[] key = new byte[16];
            random.nextBytes(key);

            int len = random.nextInt(MAX_DATA_LEN);
            byte[] originalData = new byte[len];
            random.nextBytes(originalData);

            String encryptedData = ((String) engine.eval("TEA.initkey('" + Utils.toHexString(key) + "');TEA.encrypt('" + Utils.toHexString(originalData) + "')"))
                    .toLowerCase();

            QQTea tea = new QQTea(new TeaKey(key));
            byte[] javaResult = tea.decrypt(Utils.asHexStringToBytes(encryptedData));

            Assert.assertArrayEquals(originalData, javaResult);
        }
    }
}