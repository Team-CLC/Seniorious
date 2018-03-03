package com.github.teamclc.seniorious

import com.github.teamclc.seniorious.api.user.UserStatues

class LoginInfo(
        qqID: String,
        val password: String,
        val visibility: UserStatues
) {
    val qqIDHex: ByteArray by lazy {
        intToByteArray(qqID.toLong().toInt())
    }
}