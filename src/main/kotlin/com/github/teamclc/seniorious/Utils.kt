@file:JvmName("Utils")
package com.github.teamclc.seniorious

import java.io.ByteArrayOutputStream
import java.util.*

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

fun String.asHexStringToBytes(): ByteArray {
    val t = trim().replace(" ", "")
    val bytes = ByteArray(t.length / 2)
    t.chunked(2).forEachIndexed { i, s ->
        bytes[i] = (Integer.parseInt(s, 16) and 0xFF).toByte()
    }
    return bytes
}

fun getRandomBytes(length: Int) = ByteArray(length).also(Random()::nextBytes)

fun buildByteArray(block: (ByteArrayOutputStream) -> Unit) = ByteArrayOutputStream().also(block).toByteArray()!!
fun concatByteArrays(vararg arrays: ByteArray): ByteArray {
    return ByteArray(arrays.map { it.size }.sum()).also { ret ->
        var ptr = 0
        arrays.forEach {
            System.arraycopy(it, 0, ret, ptr, it.size)
            ptr += it.size
        }
    }
}