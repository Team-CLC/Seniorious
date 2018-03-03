package com.github.teamclc.seniorious.protocol

import com.github.teamclc.seniorious.api.user.UserStatues
import com.github.teamclc.seniorious.intToByteArray
import java.net.DatagramSocket

class LoginInfo(
        qqID: String,
        val password: String,
        val visibility: UserStatues
) {
    val qqIDHex: ByteArray by lazy {
        intToByteArray(qqID.toLong().toInt())
    }
    val listeningSocket = DatagramSocket()
}