package com.example.net

import com.example.routes.aesKey
import java.security.MessageDigest
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

object SSL {

    fun genericRandom(): Int{
        // 获取熵源数据
        fun getEntropy(): ByteArray {
            // 此处可以从不同的熵源收集数据，例如鼠标移动、键盘输入、网络延迟等
            // 这里为了简单起见，使用随机字节作为熵源数据
            return Random.nextBytes(32)
        }
        // 压缩熵源数据
        fun compressEntropy(entropy: ByteArray): ByteArray {
            // 使用 SHA-256 哈希算法来压缩熵源数据
            val md = MessageDigest.getInstance("SHA-256")
            return md.digest(entropy)
        }
        // 生成种子
        fun generateSeed(compressedEntropy: ByteArray): Long {
            // 将压缩后的熵源数据转换为一个 long 类型种子
            var seed: Long = 0
            for (i in 0 until 8) {
                seed = (seed shl 8) or (compressedEntropy[i].toLong() and 0xFF)
            }
            return seed
        }
        // 生成随机数
        fun generateRandomNumber(seed: Long): Int {
            // 使用伪随机数生成器，以 seed 为种子生成随机数
            val random = Random(seed)
            return random.nextInt()
        }
        // 获取熵源数据
        val entropy = getEntropy()
        // 压缩熵源数据
        val compressedEntropy = compressEntropy(entropy)
        // 生成种子
        val seed = generateSeed(compressedEntropy)
        // 生成随机数
        return generateRandomNumber(seed)
    }

    fun hmacSHA256(key: ByteArray, data: ByteArray): ByteArray {
        val hmacKey = SecretKeySpec(key, "HmacSHA256")
        val hmac = Mac.getInstance("HmacSHA256")
        hmac.init(hmacKey)
        return hmac.doFinal(data)
    }
}