package com.github.teamclc.luna

import javax.script.ScriptEngineManager

private val jsEngine = ScriptEngineManager().getEngineByName("JS").also { it.eval(TEA_JS_CODE) }

fun teaEncrypt(data: ByteArray, key: ByteArray) = jsEngine.eval("TEA.initkey('${bytesToHexString(key)}');TEA.encrypt('${bytesToHexString(data)}')") as String
fun teaDecrypt(data: ByteArray, key: ByteArray) = jsEngine.eval("TEA.initkey('${bytesToHexString(key)}');TEA.decrypt('${bytesToHexString(data)}')") as String

private const val TEA_JS_CODE = """
var key = "",
    a = 0,
    h = [],
    z = [],
    A = 0,
    w = 0,
    o = [],
    v = [],
    p = true;
  function getRandomNumber() {
    return Math.round(Math.random() * 4294967295);
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
    return (C & 4294967295) >>> 0;
  }
  function b(C, D, B) {
    C[D + 3] = (B >> 0) & 255;
    C[D + 2] = (B >> 8) & 255;
    C[D + 1] = (B >> 16) & 255;
    C[D + 0] = (B >> 24) & 255;
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
  function encryptBytes(D) {
    h = new Array(8);
    z = new Array(8);
    A = w = 0;
    p = true;
    a = 0;
    var B = D.length;
    var E = 0;
    a = (B + 10) % 8;
    if (a != 0) {
      a = 8 - a;
    }
    o = new Array(B + a + 10);
    h[0] = ((getRandomNumber() & 248) | a) & 255;
    for (var C = 1; C <= a; C++) {
      h[C] = getRandomNumber() & 255;
    }
    a++;
    for (var C = 0; C < 8; C++) {
      z[C] = 0;
    }
    E = 1;
    while (E <= 2) {
      if (a < 8) {
        h[a++] = getRandomNumber() & 255;
        E++;
      }
      if (a == 8) {
        QQCBC();
      }
    }
    var C = 0;
    while (B > 0) {
      if (a < 8) {
        h[a++] = D[C++];
        B--;
      }
      if (a == 8) {
        QQCBC();
      }
    }
    E = 1;
    while (E <= 7) {
      if (a < 8) {
        h[a++] = 0;
        E++;
      }
      if (a == 8) {
        QQCBC();
      }
    }
    return o;
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
    a = z[0] & 7;
    E = B - a - 10;
    if (E < 0) {
      return null;
    }
    for (var D = 0; D < C.length; D++) {
      C[D] = 0;
    }
    o = new Array(E);
    w = 0;
    A = 8;
    a++;
    var G = 1;
    while (G <= 2) {
      if (a < 8) {
        a++;
        G++;
      }
      if (a == 8) {
        C = F;
        if (!g()) {
          return null;
        }
      }
    }
    var D = 0;
    while (E != 0) {
      if (a < 8) {
        o[D] = (C[w + a] ^ z[a]) & 255;
        D++;
        E--;
        a++;
      }
      if (a == 8) {
        C = F;
        w = A - 8;
        if (!g()) {
          return null;
        }
      }
    }
    for (G = 1; G < 8; G++) {
      if (a < 8) {
        if ((C[w + a] ^ z[a]) != 0) {
          return null;
        }
        a++;
      }
      if (a == 8) {
        C = F;
        w = A;
        if (!g()) {
          return null;
        }
      }
    }
    return o;
  }
  //交织算法CBC加密
  function QQCBC() {
    for (var i = 0; i < 8; i++) {
      if (p) {
        h[i] ^= z[i];
      } else {
        h[i] ^= o[w + i];
      }
    }
    var C = teaEncrypt(h);
    for (var i = 0; i < 8; i++) {
      o[A + i] = C[i] ^ z[i];
      z[i] = h[i];
    }
    w = A;
    A += 8;
    a = 0;
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
    var K = 2654435769 >>> 0;
    while (C-- > 0) {
      D += K;
      D = (D & 4294967295) >>> 0;
      H += ((G << 4) + J) ^ (G + D) ^ ((G >>> 5) + I);
      H = (H & 4294967295) >>> 0;
      G += ((H << 4) + F) ^ (H + D) ^ ((H >>> 5) + E);
      G = (G & 4294967295) >>> 0;
    }
    var L = new Array(8);
    b(L, 0, H);
    b(L, 4, G);
    return L;
  }
  function teaDecrypt(B) {
    var C = 16;
    var H = k(B, 0, 4);
    var G = k(B, 4, 4);
    var J = k(key, 0, 4);
    var I = k(key, 4, 4);
    var F = k(key, 8, 4);
    var E = k(key, 12, 4);
    var D = 3816266640 >>> 0;
    var K = 2654435769 >>> 0;
    while (C-- > 0) {
      G -= ((H << 4) + F) ^ (H + D) ^ ((H >>> 5) + E);
      G = (G & 4294967295) >>> 0;
      H -= ((G << 4) + J) ^ (G + D) ^ ((G >>> 5) + I);
      H = (H & 4294967295) >>> 0;
      D -= K;
      D = (D & 4294967295) >>> 0;
    }
    var L = new Array(8);
    b(L, 0, H);
    b(L, 4, G);
    return L;
  }
  function g() {
    var B = v.length;
    for (var C = 0; C < 8; C++) {
      z[C] ^= v[A + C];
    }
    z = teaDecrypt(z);
    A += 8;
    a = 0;
    return true;
  }
  function stringToBytes(input, isCharString) {
    var result = [];
    if (isCharString) {
      for (var i = 0; i < input.length; i++) {
        result[i] = input.charCodeAt(i) & 255; // 取ASCII转换
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