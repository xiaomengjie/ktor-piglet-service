package com.example.net

import bean.YDResponse
import com.example.sha256
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.util.*

/**
 * 网络请求
 */
object NetworkClient {

    private val httpClient = HttpClient {
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    private const val YD_TRANSLATE_APP_KEY = "1347009bafcb8933"
    private const val YD_TRANSLATE_SECRET = "V5T4XPbzd8YWwCJLxBiyoUX9Bwb5JZWo"

    suspend fun searchFromYD(keyword: String, from: String, to: String): YDResponse {
        val parameters = parameters {
            append("q", keyword)
            append("from", from)
            append("to", to)
            append("appKey", YD_TRANSLATE_APP_KEY)
            val salt = UUID.randomUUID().toString()
            append("salt", salt)
            val systemTime = (System.currentTimeMillis() / 1000).toString()
            append("curtime", systemTime)
            append("sign", "${YD_TRANSLATE_APP_KEY}${getInput(keyword)}$salt$systemTime$YD_TRANSLATE_SECRET".sha256())
            append("signType", "v3")
        }
        val httpResponse = httpClient.submitForm("https://openapi.youdao.com/api/", formParameters = parameters)
        val body = httpResponse.body<YDResponse>()
        if (body.errorCode == "411"){
            delay(500)
            return httpClient.submitForm("https://openapi.youdao.com/api/", formParameters = parameters).body<YDResponse>()
        }
        return body
    }

    private fun getInput(content: String): String {
        if (content.length <= 20) return content
        return content.substring(0, 10) + content.length + content.substring(content.length - 10, content.length)
    }
}
