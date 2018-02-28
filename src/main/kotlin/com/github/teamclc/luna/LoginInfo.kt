package com.github.teamclc.luna

class LoginInfo(
        val qqID: String,
        password: String,
        val visibility: LoginVisibility
) {
    val passwordHex: ByteArray by lazy {
        intToByteArray(qqID.toLong().toInt())
    }
}

enum class LoginVisibility(val code: Byte) {
    ONLINE(0x0A),
    Q_ME_PLEASE(0x3C),
    AFK(0x1E),
    BUSY(0x32),
    DO_NOT_DISTURB(0x46),
    INVISIBLE(0x28)
}