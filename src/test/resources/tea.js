var u = "",
    a = 0,
    h = [],
    z = [],
    A = 0,
    w = 0,
    o = [],
    v = [],
    p = true;
  function f() {
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
  function y(E) {
    if (!E) {
      return "";
    }
    var B = "";
    for (var C = 0; C < E.length; C++) {
      var D = Number(E[C]).toString(16);
      if (D.length == 1) {
        D = "0" + D;
      }
      B += D;
    }
    return B;
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
    return y(D);
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
  function j(D) {
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
    h[0] = ((f() & 248) | a) & 255;
    for (var C = 1; C <= a; C++) {
      h[C] = f() & 255;
    }
    a++;
    for (var C = 0; C < 8; C++) {
      z[C] = 0;
    }
    E = 1;
    while (E <= 2) {
      if (a < 8) {
        h[a++] = f() & 255;
        E++;
      }
      if (a == 8) {
        r();
      }
    }
    var C = 0;
    while (B > 0) {
      if (a < 8) {
        h[a++] = D[C++];
        B--;
      }
      if (a == 8) {
        r();
      }
    }
    E = 1;
    while (E <= 7) {
      if (a < 8) {
        h[a++] = 0;
        E++;
      }
      if (a == 8) {
        r();
      }
    }
    return o;
  }
  function s(F) {
    var E = 0;
    var C = new Array(8);
    var B = F.length;
    v = F;
    if (B % 8 != 0 || B < 16) {
      return null;
    }
    z = n(F);
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
  function r() {
    for (var B = 0; B < 8; B++) {
      if (p) {
        h[B] ^= z[B];
      } else {
        h[B] ^= o[w + B];
      }
    }
    var C = l(h);
    for (var B = 0; B < 8; B++) {
      o[A + B] = C[B] ^ z[B];
      z[B] = h[B];
    }
    w = A;
    A += 8;
    a = 0;
    p = false;
  }
  function l(B) {
    var C = 16;
    var H = k(B, 0, 4);
    var G = k(B, 4, 4);
    var J = k(u, 0, 4);
    var I = k(u, 4, 4);
    var F = k(u, 8, 4);
    var E = k(u, 12, 4);
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
  function n(B) {
    var C = 16;
    var H = k(B, 0, 4);
    var G = k(B, 4, 4);
    var J = k(u, 0, 4);
    var I = k(u, 4, 4);
    var F = k(u, 8, 4);
    var E = k(u, 12, 4);
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
    z = n(z);
    A += 8;
    a = 0;
    return true;
  }
  function q(F, E) {
    var D = [];
    if (E) {
      for (var C = 0; C < F.length; C++) {
        D[C] = F.charCodeAt(C) & 255;
      }
    } else {
      var B = 0;
      for (var C = 0; C < F.length; C += 2) {
        D[B++] = parseInt(F.substr(C, 2), 16);
      }
    }
    return D;
  }
  TEA = {
    encrypt: function(E, D) {
      var C = q(E, D);
      var B = j(C);
      return y(B);
    },
    enAsBase64: function(G, F) {
      var E = q(G, F);
      var D = j(E);
      var B = "";
      for (var C = 0; C < D.length; C++) {
        B += String.fromCharCode(D[C]);
      }
      return btoa(B);
    },
    decrypt: function(D) {
      var C = q(D, false);
      var B = s(C);
      return y(B);
    },
    initkey: function(B, C) {
      u = q(B, C);
    },
    bytesToStr: x,
    strToBytes: c,
    bytesInStr: y,
    dataFromStr: q
  };