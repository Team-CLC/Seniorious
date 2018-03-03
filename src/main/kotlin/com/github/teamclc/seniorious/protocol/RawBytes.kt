@file:Suppress("PropertyName")
package com.github.teamclc.seniorious.protocol

import com.github.teamclc.seniorious.asHexStringToBytes
import com.github.teamclc.seniorious.crypto.TeaKey

const val PACKET_HEAD = 0x02
const val PACKET_TAIL = 0x03

val PACKET_QQ_VER = byteArrayOf(0x37, 0x0F)
val PACKET_FIX_VER = "03 00 00 00 01 01 01 00 00 68 20 00 00 00 00".asHexStringToBytes()

val PACKET_0825_KEY = "A4 F1 91 88 C9 82 14 99 0C 9E 56 55 91 23 C8 3D".asHexStringToBytes()
val TEAKEY_0825_KEY = TeaKey(PACKET_0825_KEY)
val PACKET_0825_DATA_1 = "00 18 00 16 00 01".asHexStringToBytes()
val PACKET_0825_DATA_2 = "00 00 04 53 00 00 00 01 00 00 15 8B".asHexStringToBytes()
val PACKET_PUBLIC_KEY = "02 6D 28 41 D2 A5 6F D2 FC 3E 2A 1F 03 75 DE 6E 28 8F A8 19 3E 5F 16 49 D3".asHexStringToBytes()

val PACKET_COMMAND_LOGIN = byteArrayOf(0x08, 0x25, 0x31, 0x01)
val PACKET_DATA_LOGIN_AFTER_QQ = "00 00 00 00 03 09 00 08 00 01".asHexStringToBytes()
val PACKET_DATA_LOGIN_AFTER_IP = "00 02 00 36 00 12 00 02 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 14 00 1D 01 02 00 19".asHexStringToBytes()
val PACKET_DATA_LOGIN_BEFORE_ENCRYPTED = PACKET_FIX_VER + PACKET_0825_KEY

val PACKET_COMMAND_LOGOUT = byteArrayOf(0x00, 0x62)
val PACKET_DATA_LOGOUT_BEFORE_ENCRYPTED = " 02 00 00 00 01 01 01 00 00 68 20".asHexStringToBytes()
val PACKET_DATA_LOGOUT = ByteArray(0x10)