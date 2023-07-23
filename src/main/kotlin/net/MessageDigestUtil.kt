package com.example.xiao.piglet.tool

import java.security.MessageDigest

object MessageDigestUtil {

    private val HEX_CHARS = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    )

    fun md5(input: String): String{
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(input.encodeToByteArray())
        return toHex(messageDigest.digest())
    }

    fun sha256(content: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(content.encodeToByteArray())
        return toHex(messageDigest.digest())
    }

    private fun toHex(data: ByteArray): String {
        val r = StringBuilder(data.size * 2)
        data.forEach { b ->
            val i = b.toInt()
            r.append(HEX_CHARS[i shr 4 and 0xF])
            r.append(HEX_CHARS[i and 0xF])
        }
        return r.toString()
    }


}