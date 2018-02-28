package com.github.teamclc.luna

import javax.script.ScriptEngineManager

val jsEngine = ScriptEngineManager().getEngineByName("JS").also { it.eval(TEA_JS_CODE) }

fun teaEncrypt(data: ByteArray, key: ByteArray) = jsEngine.eval("TEA.initkey('${bytesToHexString(key)}');TEA.encrypt('${bytesToHexString(data)}')") as String
fun teaDecrypt(data: ByteArray, key: ByteArray) = jsEngine.eval("TEA.initkey('${bytesToHexString(key)}');TEA.decrypt('${bytesToHexString(data)}')") as String

private const val TEA_JS_CODE = """
var key = "",
    paddings = 0,
    temp = [],
    z = [],
    A = 0,
    w = 0,
    result = [],
    v = [],
    p = true;
function getRandomNumber() {
    return Math.round(Math.random() * 0xffffffff);
}
function k(E, F, B) {
    if (!B || B > 4) {
        B = 4;
    }
    var C = 0;
    for (var D = F; D < F + B; D++) {
        C <<= 8;
        C |= E[D];
    }
    return (C & 0xffffffff) >>> 0;
}
function b(C, D, B) {
    C[D + 3] = (B >> 0) & 0xFF;
    C[D + 2] = (B >> 8) & 0xFF;
    C[D + 1] = (B >> 16) & 0xFF;
    C[D + 0] = (B >> 24) & 0xFF;
}
function bytesToHexString(bytes) {
    if (!bytes) {
        return "";
    }
    var result = "";
    for (var i = 0; i < bytes.length; i++) {
        var curByte = Number(bytes[i]).toString(16);
        if (curByte.length == 1) {
            curByte = "0" + curByte;
        }
        result += curByte;
    }
    return result;
}
function x(C) {
    var D = "";
    for (var B = 0; B < C.length; B += 2) {
        D += String.fromCharCode(parseInt(C.substr(B, 2), 16));
    }
    return D;
}
function c(E, B) {
    if (!E) {
        return "";
    }
    if (B) {
        E = m(E);
    }
    var D = [];
    for (var C = 0; C < E.length; C++) {
        D[C] = E.charCodeAt(C);
    }
    return bytesToHexString(D);
}
function m(E) {
    var D,
        F,
        C = [],
        B = E.length;
    for (D = 0; D < B; D++) {
        F = E.charCodeAt(D);
        if (F > 0 && F <= 127) {
            C.push(E.charAt(D));
        } else {
            if (F >= 128 && F <= 2047) {
                C.push(
                    String.fromCharCode(192 | ((F >> 6) & 31)),
                    String.fromCharCode(128 | (F & 63))
                );
            } else {
                if (F >= 2048 && F <= 65535) {
                    C.push(
                        String.fromCharCode(224 | ((F >> 12) & 15)),
                        String.fromCharCode(128 | ((F >> 6) & 63)),
                        String.fromCharCode(128 | (F & 63))
                    );
                }
            }
        }
    }
    return C.join("");
}
function encryptBytes(input) {
    temp = new Array(8);
    z = new Array(8);
    A = w = 0;
    p = true;
    paddings = 0;
    var inputLen = input.length;
    var E = 0;
    paddings = (inputLen + 10) % 8;
    if (paddings != 0) {
        paddings = 8 - paddings;
    }
    result = new Array(inputLen + paddings + 10);
    temp[0] = ((getRandomNumber() & 0xF8) | paddings) & 0xFF;
    for (var i = 1; i <= paddings; i++) {
        temp[i] = getRandomNumber() & 0xFF;
    }
    paddings++;
    for (var i = 0; i < 8; i++) {
        z[i] = 0;
    }
    E = 1;
    while (E <= 2) {
        if (paddings < 8) {
            temp[paddings++] = getRandomNumber() & 0xFF;
            E++;
        }
        if (paddings == 8) {
            QQCBC();
        }
    }
    var i = 0;
    while (inputLen > 0) {
        if (paddings < 8) {
            temp[paddings++] = input[i++];
            inputLen--;
        }
        if (paddings == 8) {
            QQCBC();
        }
    }
    E = 1;
    while (E <= 7) {
        if (paddings < 8) {
            temp[paddings++] = 0;
            E++;
        }
        if (paddings == 8) {
            QQCBC();
        }
    }
    return result;
}
function decryptBytes(F) {
    var E = 0;
    var C = new Array(8);
    var B = F.length;
    v = F;
    if (B % 8 != 0 || B < 16) {
        return null;
    }
    z = teaDecrypt(F);
    paddings = z[0] & 7;
    E = B - paddings - 10;
    if (E < 0) {
        return null;
    }
    for (var D = 0; D < C.length; D++) {
        C[D] = 0;
    }
    result = new Array(E);
    w = 0;
    A = 8;
    paddings++;
    var G = 1;
    while (G <= 2) {
        if (paddings < 8) {
            paddings++;
            G++;
        }
        if (paddings == 8) {
            C = F;
            if (!g()) {
                return null;
            }
        }
    }
    var D = 0;
    while (E != 0) {
        if (paddings < 8) {
            result[D] = (C[w + paddings] ^ z[paddings]) & 0xFF;
            D++;
            E--;
            paddings++;
        }
        if (paddings == 8) {
            C = F;
            w = A - 8;
            if (!g()) {
                return null;
            }
        }
    }
    for (G = 1; G < 8; G++) {
        if (paddings < 8) {
            if ((C[w + paddings] ^ z[paddings]) != 0) {
                return null;
            }
            paddings++;
        }
        if (paddings == 8) {
            C = F;
            w = A;
            if (!g()) {
                return null;
            }
        }
    }
    return result;
}
//交织算法CBC加密
function QQCBC() {
    for (var i = 0; i < 8; i++) {
        if (p) {
            temp[i] ^= z[i];
        } else {
            temp[i] ^= result[w + i];
        }
    }
    var C = teaEncrypt(temp);
    for (var i = 0; i < 8; i++) {
        result[A + i] = C[i] ^ z[i];
        z[i] = temp[i];
    }
    w = A;
    A += 8;
    paddings = 0;
    p = false;
}
function teaEncrypt(B) {
    var C = 16;
    var H = k(B, 0, 4);
    var G = k(B, 4, 4);
    var J = k(key, 0, 4);
    var I = k(key, 4, 4);
    var F = k(key, 8, 4);
    var E = k(key, 12, 4);
    var D = 0;
    var K = 0x9e3779b9 >>> 0;
    while (C-- > 0) {
        D += K;
        D = (D & 0xffffffff) >>> 0;
        H += ((G << 4) + J) ^ (G + D) ^ ((G >>> 5) + I);
        H = (H & 0xffffffff) >>> 0;
        G += ((H << 4) + F) ^ (H + D) ^ ((H >>> 5) + E);
        G = (G & 0xffffffff) >>> 0;
    }
    var L = new Array(8);
    b(L, 0, H);
    b(L, 4, G);
    return L;
}
function teaDecrypt(bytes) {
    var remainingRounds = 16;
    var H = k(bytes, 0, 4);
    var G = k(bytes, 4, 4);
    var J = k(key, 0, 4);
    var I = k(key, 4, 4);
    var F = k(key, 8, 4);
    var E = k(key, 12, 4);
    var D = 0xe3779b90 >>> 0;
    var K = 0x9e3779b9 >>> 0;
    while (remainingRounds-- > 0) {
        G -= ((H << 4) + F) ^ (H + D) ^ ((H >>> 5) + E);
        G = (G & 0xffffffff) >>> 0;
        H -= ((G << 4) + J) ^ (G + D) ^ ((G >>> 5) + I);
        H = (H & 0xffffffff) >>> 0;
        D -= K;
        D = (D & 0xffffffff) >>> 0;
    }
    var L = new Array(8);
    b(L, 0, H);
    b(L, 4, G);
    return L;
}
function g() {
    var B = v.length;
    for (var i = 0; i < 8; i++) {
        z[i] ^= v[A + i];
    }
    z = teaDecrypt(z);
    A += 8;
    paddings = 0;
    return true;
}
function stringToBytes(input, isCharString) {
    var result = [];
    if (isCharString) {
        for (var i = 0; i < input.length; i++) {
            result[i] = input.charCodeAt(i) & 0xFF; // 取ASCII转换
        }
    } else {
        var bytePos = 0;
        for (var i = 0; i < input.length; i += 2) {
            result[bytePos++] = parseInt(input.substr(i, 2), 16); // 视为16进制字符串转换
        }
    }
    return result;
}
TEA = {
    encrypt: function(input, isCharString) {
        var stringBytes = stringToBytes(input, isCharString);
        var resultBytes = encryptBytes(stringBytes);
        return bytesToHexString(resultBytes);
    },
    encryptAsBase64: function(input, isCharString) {
        var stringBytes = stringToBytes(input, isCharString);
        var resultBytes = encryptBytes(stringBytes);
        var rawString = "";
        for (var i = 0; i < resultBytes.length; i++) {
            rawString += String.fromCharCode(resultBytes[i]);
        }
        return btoa(rawString);
    },
    decrypt: function(input) {
        var stringBytes = stringToBytes(input, false);
        var resultBytes = decryptBytes(stringBytes);
        return bytesToHexString(resultBytes);
    },
    initkey: function(input, isCharString) {
        key = stringToBytes(input, isCharString);
    },
    bytesToStr: x,
    strToBytes: c,
    bytesInStr: bytesToHexString,
    dataFromStr: stringToBytes
};

        """