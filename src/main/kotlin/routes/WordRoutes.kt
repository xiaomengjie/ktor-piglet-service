package routes

import com.example.database.dao.wordDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.bean.Response
import models.Word

fun Route.wordRoutes(){
    route("/word"){

        get{
            call.request.queryParameters["english"]?.let {
                val result = wordDao.queryWord(it, "english")
                call.respond(
                    Response("OK", HttpStatusCode.OK.value, result)
                )
                return@get
            }

            call.request.queryParameters["chinese"]?.let {
                val result = wordDao.queryWord(it, "chinese")
                call.respond(
                    Response("OK", HttpStatusCode.OK.value, result)
                )
            }
        }

        post {
            val receive = call.receive<Word>()
            val result = wordDao.increaseWord(receive)
            call.respond(
                Response("OK", HttpStatusCode.OK.value, result)
            )
        }
    }
}