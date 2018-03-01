package com.github.teamclc.luna

import javax.script.ScriptEngineManager

val jsEngine = ScriptEngineManager().getEngineByName("JS").also { it.eval(TEA_JS_CODE) }

fun teaEncrypt(data: ByteArray, key: ByteArray) = jsEngine.eval("TEA.initkey('${bytesToHexString(key)}');TEA.encrypt('${bytesToHexString(data)}')") as String
fun teaDecrypt(data: ByteArray, key: ByteArray) = jsEngine.eval("TEA.initkey('${bytesToHexString(key)}');TEA.decrypt('${bytesToHexString(data)}')") as String

private const val TEA_JS_CODE = """
var key = "",
    paddings = 0,
    current = [],
    IVOrLastBlock = [],
    cbcPtr = 0,
    cbcPtr2 = 0,
    CBCResult = [],
    decryptTempV = [],
    isFirstBlock = true;
function getRandomNumber() {
    return Math.round(Math.random() * 0xffffffff);
}
function bytesToInt(bytes, offset, len) {
    if (!len || len > 4) {
        len = 4;
    }
    var res = 0;
    for (var i = offset; i < offset + len; i++) {
        res <<= 8;
        res |= bytes[i];
    }
    return (res & 0xffffffff) >>> 0;
}
function intToBytes(bytes, offset, num) {
    bytes[offset + 3] = (num >> 0) & 0xFF;
    bytes[offset + 2] = (num >> 8) & 0xFF;
    bytes[offset + 1] = (num >> 16) & 0xFF;
    bytes[offset + 0] = (num >> 24) & 0xFF;
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
    current = new Array(8);
    IVOrLastBlock = new Array(8);
    cbcPtr = cbcPtr2 = 0;
    isFirstBlock = true;
    paddings = 0;
    var inputLen = input.length;
    var E = 0;
    paddings = (inputLen + 10) % 8;
    if (paddings != 0) {
        paddings = 8 - paddings;
    }
    CBCResult = new Array(inputLen + paddings + 10);
    current[0] = ((getRandomNumber() & 0xF8) | paddings) & 0xFF;
    for (var i = 1; i <= paddings; i++) {
        current[i] = getRandomNumber() & 0xFF;
    }
    paddings++;
    for (var i = 0; i < 8; i++) {
        IVOrLastBlock[i] = 0;
    }
    E = 1;
    while (E <= 2) {
        if (paddings < 8) {
            current[paddings++] = getRandomNumber() & 0xFF;
            E++;
        }
        if (paddings == 8) {
            QQCBC();
        }
    }
    var i = 0;
    while (inputLen > 0) {
        if (paddings < 8) {
            current[paddings++] = input[i++];
            inputLen--;
        }
        if (paddings == 8) {
            QQCBC();
        }
    }
    E = 1;
    while (E <= 7) {
        if (paddings < 8) {
            current[paddings++] = 0;
            E++;
        }
        if (paddings == 8) {
            QQCBC();
        }
    }
    return CBCResult;
}
function decryptBytes(input) {
    var temp = 0;
    var tempBuf = new Array(8);
    var inputLen = input.length;
    decryptTempV = input;
    if (inputLen % 8 != 0 || inputLen < 16) {
        return null;
    }
    IVOrLastBlock = teaDecrypt(input);
    paddings = IVOrLastBlock[0] & 7;
    temp = inputLen - paddings - 10;
    if (temp < 0) {
        return null;
    }
    for (var D = 0; D < tempBuf.length; D++) {
        tempBuf[D] = 0;
    }
    CBCResult = new Array(temp);
    cbcPtr2 = 0;
    cbcPtr = 8;
    paddings++;
    var G = 1;
    while (G <= 2) {
        if (paddings < 8) {
            paddings++;
            G++;
        }
        if (paddings == 8) {
            tempBuf = input;
            if (!g()) {
                return null;
            }
        }
    }
    var D = 0;
    while (temp != 0) {
        if (paddings < 8) {
            CBCResult[D] = (tempBuf[cbcPtr2 + paddings] ^ IVOrLastBlock[paddings]) & 0xFF;
            D++;
            temp--;
            paddings++;
        }
        if (paddings == 8) {
            tempBuf = input;
            cbcPtr2 = cbcPtr - 8;
            if (!g()) {
                return null;
            }
        }
    }
    for (G = 1; G < 8; G++) {
        if (paddings < 8) {
            if ((tempBuf[cbcPtr2 + paddings] ^ IVOrLastBlock[paddings]) != 0) {
                return null;
            }
            paddings++;
        }
        if (paddings == 8) {
            tempBuf = input;
            cbcPtr2 = cbcPtr;
            if (!g()) {
                return null;
            }
        }
    }
    return CBCResult;
}
//交织算法CBC加密
function QQCBC() {
    for (var i = 0; i < 8; i++) {
        if (isFirstBlock) {
            current[i] ^= IVOrLastBlock[i];
        } else {
            current[i] ^= CBCResult[cbcPtr2 + i];
        }
    }
    var C = teaEncrypt(current);
    for (var i = 0; i < 8; i++) {
        CBCResult[cbcPtr + i] = C[i] ^ IVOrLastBlock[i];
        IVOrLastBlock[i] = current[i];
    }
    cbcPtr2 = cbcPtr;
    cbcPtr += 8;
    paddings = 0;
    isFirstBlock = false;
}
function teaEncrypt(input) {
    var remainingRounds = 16;
    var low = bytesToInt(input, 0, 4);
    var high = bytesToInt(input, 4, 4);
    var key1 = bytesToInt(key, 0, 4);
    var key2 = bytesToInt(key, 4, 4);
    var key3 = bytesToInt(key, 8, 4);
    var key4 = bytesToInt(key, 12, 4);
    var sum = 0;
    var delta = 0x9e3779b9 >>> 0;
    while (remainingRounds-- > 0) {
        sum += delta;
        sum = (sum & 0xffffffff) >>> 0;
        low += ((high << 4) + key1) ^ (high + sum) ^ ((high >>> 5) + key2);
        low = (low & 0xffffffff) >>> 0;
        high += ((low << 4) + key3) ^ (low + sum) ^ ((low >>> 5) + key4);
        high = (high & 0xffffffff) >>> 0;
    }
    var output = new Array(8);
    intToBytes(output, 0, low);
    intToBytes(output, 4, high);
    return output;
}
function teaDecrypt(bytes) {
    var remainingRounds = 16;
    var low = bytesToInt(bytes, 0, 4);
    var high = bytesToInt(bytes, 4, 4);
    var key1 = bytesToInt(key, 0, 4);
    var key2 = bytesToInt(key, 4, 4);
    var key3 = bytesToInt(key, 8, 4);
    var key4 = bytesToInt(key, 12, 4);
    var sum = 0xe3779b90 >>> 0;
    var delta2 = 0x9e3779b9 >>> 0;
    while (remainingRounds-- > 0) {
        high -= ((low << 4) + key3) ^ (low + sum) ^ ((low >>> 5) + key4);
        high = (high & 0xffffffff) >>> 0;
        low -= ((high << 4) + key1) ^ (high + sum) ^ ((high >>> 5) + key2);
        low = (low & 0xffffffff) >>> 0;
        sum -= delta2;
        sum = (sum & 0xffffffff) >>> 0;
    }
    var L = new Array(8);
    intToBytes(L, 0, low);
    intToBytes(L, 4, high);
    return L;
}
function g() {
    var B = decryptTempV.length;
    for (var i = 0; i < 8; i++) {
        IVOrLastBlock[i] ^= decryptTempV[cbcPtr + i];
    }
    IVOrLastBlock = teaDecrypt(IVOrLastBlock);
    cbcPtr += 8;
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