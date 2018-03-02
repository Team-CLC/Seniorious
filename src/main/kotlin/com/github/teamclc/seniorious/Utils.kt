@file:JvmName("Utils")
package com.github.teamclc.seniorious

fun intToByteArray(a: Int) = byteArrayOf(
        (a shr 24 and 0xFF).toByte(),
        (a shr 16 and 0xFF).toByte(),
        (a shr 8 and 0xFF).toByte(),
        (a and 0xFF).toByte()
)

fun ByteArray.toHexString(): String {
    val receiver = this
    return buildString {
        receiver.asSequence().map { (it.toInt() and 0xFF).toString(16) }.forEach {
            if (it.length == 1)
                append('0')
            append(it)
        }
    }
}

fun String.asHexStringToByteArray(): ByteArray {
    val t = trim()
    val bytes = ByteArray(t.length / 2)
    var i = 0
    while (i < t.length / 2) {
        bytes[i] = (Integer.parseInt(t.substring(i * 2, i * 2 + 2), 16) and 0xFF).toByte()
        i++
    }
    return bytes
}