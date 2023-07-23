package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.bean.Response
import com.example.db.dao.word.wordDao
import models.Word

fun Route.wordRoutes(){
    route("/word"){

        //content-type: application/json
        post("/insert") {
            val words = call.receive<List<Word>>()
            call.respond(
                wordDao.increaseWords(words).run {
                    if (this){
                        Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, true)
                    }else{
                        Response(HttpStatusCode.BadRequest.description, HttpStatusCode.BadRequest.value, false)
                    }
                }
            )
        }

        //content-type: application/json
        post("/delete") {
            val englishes = call.receive<List<String>>()
            call.respond(
                wordDao.deleteWords(englishes).run {
                    if (this){
                        Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, true)
                    }else{
                        Response(HttpStatusCode.BadRequest.description, HttpStatusCode.BadRequest.value, false)
                    }
                }
            )
        }

        get("/query") {
            val english = call.request.queryParameters["english"]
            val chinese = call.request.queryParameters["chinese"]
            //英译汉
            if (!english.isNullOrEmpty()){
                call.respond(
                    Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, wordDao.queryWords(english = english))
                )
                return@get
            }
            //汉译英
            if (!chinese.isNullOrEmpty()){
                call.respond(
                    Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, wordDao.queryWords(chinese = chinese))
                )
                return@get
            }
            //没有参数，查全部
            call.respond(
                Response(HttpStatusCode.OK.description, HttpStatusCode.OK.value, wordDao.queryWords())
            )
        }
    }
}