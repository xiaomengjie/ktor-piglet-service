package com.example

import com.example.db.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import com.example.plugins.*
import io.ktor.server.tomcat.*

fun main() {
    embeddedServer(Tomcat, port = 8080, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    //数据库初始化
    DatabaseFactory.init()
    //路由配置
    configureRouting()
    //序列化配置
    configureSerialization()
}
