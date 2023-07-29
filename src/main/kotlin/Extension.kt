package com.example

import com.example.routes.aesKey
import com.example.xiao.piglet.tool.MessageDigestUtil
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

//将字符串sha256
fun String.sha256(): String = MessageDigestUtil.sha256(this)

/**
 * 对字符串进行AES加密，结果经过Base64编码
 */
fun String.aesEncrypt(): String{
    if (aesKey.isEmpty()) return this
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    val secretKeySpec = SecretKeySpec(aesKey.toByteArray(), "AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
    val bytes = cipher.doFinal(this.toByteArray())
    return Base64.getEncoder().encodeToString(bytes)
}

/**
 * 对Base64编码的字符串进行解码，然后用AES解密
 */
fun String.aesDecrypt(): String{
    if (aesKey.isEmpty()) return this
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    val secretKeySpec = SecretKeySpec(aesKey.toByteArray(), "AES")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
    val bytes = cipher.doFinal(Base64.getDecoder().decode(this))
    return bytes.decodeToString()
}

//字符串集合用换行符拼接
fun List<String>.toTextWithLine(): String{
    val stringBuilder = StringBuilder()
    this.forEach {
        stringBuilder.appendLine(it)
    }
    return stringBuilder.toString()
}