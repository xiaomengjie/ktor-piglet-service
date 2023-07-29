package com.example.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

var aesKey = ""
fun Route.ssl(){
    route("/ssl"){
        post("/client_hello") {
            val certificate = File("./ssl/ssl_certificate.pem").bufferedReader().use {
                it.readText()
            }.replace(
                "-----BEGIN CERTIFICATE-----", ""
            ).replace(
                "-----END CERTIFICATE-----", ""
            ).replace("\n", "").trim()
            call.respondBytes {
                Base64.getDecoder().decode(certificate)
            }
        }
        post("/aes_key") {
            val body = call.receive<ByteArray>()
            val privateKeyString = File("./ssl/private_key.pem").bufferedReader().use {
                it.readText()
            }.replace(
                "-----BEGIN PRIVATE KEY-----", ""
            ).replace(
                "-----END PRIVATE KEY-----", ""
            ).replace("\n", "").trim()
            val byteArrayKey = Base64.getDecoder().decode(privateKeyString)
            val pkcS8EncodedKeySpec = PKCS8EncodedKeySpec(byteArrayKey)
            val privateKey = KeyFactory.getInstance("RSA").generatePrivate(pkcS8EncodedKeySpec)
            val cipher = Cipher.getInstance("RSA")
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            val bytes = cipher.doFinal(body)
            aesKey = bytes.decodeToString()
            call.respond(HttpStatusCode.OK)
        }
    }
}

