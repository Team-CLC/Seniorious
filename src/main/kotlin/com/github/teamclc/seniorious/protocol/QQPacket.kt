package com.github.teamclc.seniorious.protocol

import com.github.teamclc.seniorious.buildByteArray
import com.github.teamclc.seniorious.crypto.TeaKey
import com.github.teamclc.seniorious.getRandomBytes
import java.util.*

@Suppress("MemberVisibilityCanPrivate")
data class QQPacket (
        val command: ByteArray,
        val qqNumber: ByteArray,
        val encryptingData: ByteArray,
        val encryptKey: TeaKey,
        val sendSequenceNumber: Boolean = true,
        val extraDataBeforeEncrypedData: ByteArray? = null
) {
    fun toByteArray(): ByteArray {
        return buildByteArray {
            it.write(PACKET_HEAD)
            it.write(PACKET_QQ_VER)
            if (sendSequenceNumber)
                it.write(getRandomBytes(2))
            it.write(qqNumber)

            extraDataBeforeEncrypedData?.let(it::write)
            it.write(teaEncrypt(encryptingData, encryptKey))

            it.write(PACKET_TAIL)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QQPacket

        if (!Arrays.equals(command, other.command)) return false
        if (!Arrays.equals(qqNumber, other.qqNumber)) return false
        if (!Arrays.equals(encryptingData, other.encryptingData)) return false
        if (encryptKey != other.encryptKey) return false
        if (sendSequenceNumber != other.sendSequenceNumber) return false
        if (!Arrays.equals(extraDataBeforeEncrypedData, other.extraDataBeforeEncrypedData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(command)
        result = 31 * result + Arrays.hashCode(qqNumber)
        result = 31 * result + Arrays.hashCode(encryptingData)
        result = 31 * result + encryptKey.hashCode()
        result = 31 * result + sendSequenceNumber.hashCode()
        result = 31 * result + (extraDataBeforeEncrypedData?.let { Arrays.hashCode(it) } ?: 0)
        return result
    }
}