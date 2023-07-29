package com.example.plugins

import com.example.routes.ssl
import io.ktor.server.routing.*
import io.ktor.server.application.*
import routes.passwordRoutes
import routes.wordRoutes

fun Application.configureRouting() {
    routing {
        ssl()
        passwordRoutes()
        wordRoutes()
    }
}
