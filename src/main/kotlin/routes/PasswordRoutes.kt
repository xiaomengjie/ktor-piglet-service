package routes

import com.example.db.dao.passwordDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.Password
import com.example.bean.Response

fun Route.passwordRoutes(){
    route("/password"){

        post("/insert") {
            val password = call.receive<Password>()
            call.respond(
                Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, passwordDao.increasePassword(password))
            )
        }

        post("/delete") {
            val name = call.receive<String>()
            if (passwordDao.deletePassword(name)){
                call.respond(Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, null))
            }else{
                call.respond(Response(HttpStatusCode.BadRequest.description, HttpStatusCode.BadRequest.value, null))
            }
        }

        post("/deleteAll") {
            if (passwordDao.deleteAllPassword()){
                call.respond(Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, null))
            }else{
                call.respond(Response(HttpStatusCode.BadRequest.description, HttpStatusCode.BadRequest.value, null))
            }
        }

        post("/update") {
            val password = call.receive<Password>()
            call.respond(
                Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, passwordDao.updatePassword(password))
            )
        }

        get("/query") {
            val name = call.request.queryParameters["name"]?: return@get call.respond(
                Response("Missing parameter name", HttpStatusCode.BadRequest.value, null)
            )
            call.respond(
                Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, passwordDao.queryPassword(name))
            )
        }

        get("/queryAll") {
            call.respond(
                Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, passwordDao.queryAllPassword())
            )
        }
    }
}