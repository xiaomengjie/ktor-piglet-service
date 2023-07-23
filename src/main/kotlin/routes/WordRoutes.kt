package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.bean.Response
import com.example.db.dao.wordDao
import models.Word

fun Route.wordRoutes(){
    route("/word"){

        post("/insert") {
            val word = call.receive<Word>()
            call.respond(
                Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, wordDao.increaseWord(word))
            )
        }

        post("/delete") {
            val english = call.receiveParameters()["english"].toString()
            call.respond(
                Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, wordDao.deleteWord(english))
            )
        }

        post("/deleteAll") {
            call.respond(
                Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, wordDao.deleteAllWord())
            )
        }

        post("/update") {
            val word = call.receive<Word>()
            call.respond(
                Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, wordDao.updateWord(word))
            )
        }

        get("/query") {
            val english = call.request.queryParameters["english"]
            val chinese = call.request.queryParameters["chinese"]
            //英译汉
            if (!english.isNullOrEmpty()){
                call.respond(
                    Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, wordDao.queryWord(english, "english"))
                )
                return@get
            }
            //汉译英
            if (!chinese.isNullOrEmpty()){
                call.respond(
                    Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, wordDao.queryWord(chinese, "chinese"))
                )
                return@get
            }
            //没有参数
            call.respond(
                Response("Missing parameter english/chinese", HttpStatusCode.BadRequest.value, null)
            )
        }

        get("/queryAll") {
            call.respond(
                Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, wordDao.queryAllWord())
            )
        }
    }
}