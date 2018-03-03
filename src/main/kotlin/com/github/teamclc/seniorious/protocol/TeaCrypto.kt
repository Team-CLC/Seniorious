package com.github.teamclc.seniorious.protocol

import com.github.teamclc.seniorious.crypto.QQTea
import com.github.teamclc.seniorious.crypto.TeaKey

fun teaEncrypt(data: ByteArray, key: ByteArray) = teaEncrypt(data, TeaKey(key))
fun teaDecrypt(data: ByteArray, key: ByteArray) = teaDecrypt(data, TeaKey(key))

fun teaEncrypt(data: ByteArray, key: TeaKey) = QQTea(key).encrypt(data)!!
fun teaDecrypt(data: ByteArray, key: TeaKey) = QQTea(key).decrypt(data)!!