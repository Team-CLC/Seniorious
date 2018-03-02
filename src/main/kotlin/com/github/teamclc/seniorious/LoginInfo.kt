package com.github.teamclc.seniorious

class LoginInfo(
        qqID: String,
        val password: String,
        val visibility: LoginVisibility
) {
    val qqIDHex: ByteArray by lazy {
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