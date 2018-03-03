package com.github.teamclc.seniorious.protocol

import com.github.teamclc.seniorious.concatByteArrays
import com.github.teamclc.seniorious.crypto.TeaKey
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress

class QQContext {
    private val udpSocket = DatagramSocket()

    @Volatile
    var status = LoginStatus.NOT_LOGGED_IN

    private var sessionKey: TeaKey? = null
    private var loginInfo: LoginInfo? = null
    fun login(loginInfo: LoginInfo) {
        if (status == LoginStatus.LOGGING_IN)
            throw IllegalStateException("Don't send duplicate login request !")
        if (status == LoginStatus.LOGGED_IN)
            logout()

        if (udpSocket.isConnected)
            udpSocket.disconnect()

        udpSocket.bind(InetSocketAddress(0))
        status = LoginStatus.LOGGING_IN
        udpSocket.connect(loginInfo.server, QQ_SERVER_PORT)
        sendPacket(QQPacket(
                PACKET_COMMAND_LOGIN,
                loginInfo.qqNumberHex,
                concatByteArrays(
                        PACKET_0825_DATA_1, PACKET_0825_DATA_2,
                        loginInfo.qqNumberHex,
                        PACKET_DATA_LOGIN_AFTER_QQ,
                        loginInfo.server.address,
                        PACKET_DATA_LOGIN_AFTER_IP,
                        PACKET_PUBLIC_KEY
                ),
                TEAKEY_0825_KEY,
                sendSequenceNumber = false,
                extraDataBeforeEncrypedData = PACKET_DATA_LOGIN_BEFORE_ENCRYPTED
        ))
        this.loginInfo = loginInfo

        //udpSocket.receive()
    }

    fun logout() {
        val tkey = sessionKey
        sessionKey = null
        if (status != LoginStatus.LOGGED_IN) return
        if (tkey == null || loginInfo == null) return
        sendPacket(QQPacket(
                PACKET_COMMAND_LOGOUT,
                loginInfo!!.qqNumberHex,
                PACKET_DATA_LOGOUT,
                tkey,
                extraDataBeforeEncrypedData = PACKET_DATA_LOGOUT_BEFORE_ENCRYPTED
        ))
        loginInfo = null
        udpSocket.disconnect()
    }

    fun sendPacket(packet: QQPacket) {
        if (!udpSocket.isConnected || !udpSocket.isBound) throw IllegalStateException()
        val buf = packet.toByteArray()
        udpSocket.send(DatagramPacket(buf, 0, buf.size, udpSocket.inetAddress, QQ_SERVER_PORT))
    }
}

enum class UserVisibility(val id: Byte) {
    ONLINE(0x0A),
    Q_ME_PLEASE(0x3C),
    AFK(0x1E),
    BUSY(0x32),
    DO_NOT_DISTURB(0x46),
    INVISIBLE(0x28)
}

enum class LoginStatus {
    NOT_LOGGED_IN,
    LOGGED_IN,

    LOGGING_IN,

    LOGIN_FAILED,
    LOGGED_OUT
}