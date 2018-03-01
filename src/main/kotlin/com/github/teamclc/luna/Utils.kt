@file:JvmName("Utils")
package com.github.teamclc.luna

fun intToByteArray(a: Int) = byteArrayOf(
        (a shr 24 and 0xFF).toByte(),
        (a shr 16 and 0xFF).toByte(),
        (a shr 8 and 0xFF).toByte(),
        (a and 0xFF).toByte()
)

fun bytesToHexString(byteArray: ByteArray): String {
    return buildString {
        byteArray.asSequence().map { (it.toInt() and 0xFF).toString(16) }.forEach {
            if (it.length == 1)
                append('0')
            append(it)
        }
    }
}