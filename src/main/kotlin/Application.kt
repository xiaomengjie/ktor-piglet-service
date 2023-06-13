package com.example

import com.example.db.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import com.example.plugins.*
import io.ktor.server.tomcat.*

fun main() {

//    //生成证书
//    val keyStoreFile = File("build/keystore.jks")
//    val keyStore = buildKeyStore {
//        certificate("saveps"){
//            password = "saveps"
//        }
//    }
//    keyStore.saveToFile(keyStoreFile, "123456")
//
//    val environment = applicationEngineEnvironment {
//        log = LoggerFactory.getLogger("ktor.application")
//        sslConnector(
//            keyStore = keyStore,
//            keyAlias = "saveps",
//            keyStorePassword = { "123456".toCharArray() },
//            privateKeyPassword = { "saveps".toCharArray() },
//            {
//                port = 8443
//                keyStorePath = keyStoreFile
//            }
//        )
//        module(Application::module)
//    }

    embeddedServer(Tomcat, port = 8080, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureRouting()
    configureSerialization()
}
