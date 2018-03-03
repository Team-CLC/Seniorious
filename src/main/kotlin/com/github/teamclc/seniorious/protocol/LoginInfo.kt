package com.github.teamclc.seniorious.protocol

import com.github.teamclc.seniorious.api.user.UserStatus
import com.github.teamclc.seniorious.intToByteArray
import java.net.InetAddress

class LoginInfo(
        val qqNumber: String,
        val password: String,
        val visibility: UserStatus,
        val server: InetAddress = qqServers.values.first()
) {
    val qqNumberHex: ByteArray by lazy {
        intToByteArray(qqNumber.toLong().toInt())
    }
}