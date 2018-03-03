package com.github.teamclc.seniorious.protocol

import java.net.InetAddress

const val QQ_SERVER_PORT = 8000

val qqServers: MutableMap<String, InetAddress> by lazy {
    LinkedHashMap<String, InetAddress>().also {
        updateQQServers(it)
    }
}

private val qqServerSZ1 = InetAddress.getByName("183.60.56.29")
fun updateQQServers(map: MutableMap<String, InetAddress>) {
    repeat(9) {
        map["sz${it + 1}.tencent.com"] =
                if (it == 0 || it == 6)
                    qqServerSZ1
                else
                    InetAddress.getByName("sz${it + 1}.tencent.com")
    }
}