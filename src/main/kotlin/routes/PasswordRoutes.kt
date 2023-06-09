package routes

import com.example.database.dao.passwordDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import models.Password
import com.example.bean.Response

fun Route.passwordRoutes(){
    route("/password"){

        get {
            val name = call.request.queryParameters["name"]?: return@get call.respond(
                Response("Missing parameter name", HttpStatusCode.BadRequest.value, null)
            )
            val result = passwordDao.queryPassword(name)
            call.respond(
                Response(
                    if (result == null) "No Password Found" else "OK",
                    if (result == null) HttpStatusCode.NotFound.value else HttpStatusCode.OK.value,
                    result
                )
            )
        }

        post {
            val password = call.receive<Password>()
            val result = passwordDao.increasePassword(password)
            call.respond(
                Response(
                    if (result == null) "Increase Failure" else "OK",
                    if (result == null) HttpStatusCode.BadRequest.value else HttpStatusCode.OK.value,
                    result
                )
            )
        }
    }
}