package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.security.KeyFactory
import java.security.PublicKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

var aesKey = ""

private val privateKeyFilePath = "/root/private_key.pem"
private val certificateFilePath = "/root/ssl_certificate.pem"

//private val privateKeyFilePath = "./ssl/private_key.pem"
//private val certificateFilePath = "./ssl/ssl_certificate.pem"

fun Route.ssl(){
    route("/ssl"){
        post("/client_hello") {
            val certificate = File(certificateFilePath).bufferedReader().use {
                it.readText()
            }.replace(
                "-----BEGIN CERTIFICATE-----", ""
            ).replace(
                "-----END CERTIFICATE-----", ""
            ).replace("\n", "").trim()
            val certificateByteArray = Base64.getDecoder().decode(certificate)
            call.respondBytes {
                certificateByteArray
            }
        }
        post("/aes_key") {
            //获取到服务器传送过来的进过RSA公钥加密的AES密钥
            val key = call.receive<ByteArray>()
            //将私钥文件取出标签
            val privateKeyString = File(privateKeyFilePath).bufferedReader().use {
                it.readText()
            }.replace(
                "-----BEGIN PRIVATE KEY-----", ""
            ).replace(
                "-----END PRIVATE KEY-----", ""
            ).replace("\n", "").trim()
            //base64解码得到私钥文件
            val byteArrayKey = Base64.getDecoder().decode(privateKeyString)
            //通过KeyFactory获取到私钥
            val pkcS8EncodedKeySpec = PKCS8EncodedKeySpec(byteArrayKey)
            val privateKey = KeyFactory.getInstance("RSA").generatePrivate(pkcS8EncodedKeySpec)
            //私钥解密获取到AES密钥
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            val bytes = cipher.doFinal(key)
            //AES密钥转String
            aesKey = bytes.decodeToString()
            call.respond(HttpStatusCode.OK)
        }
        post("/key_clean") {
            aesKey = ""
            call.respond(HttpStatusCode.OK)
        }
    }
}

