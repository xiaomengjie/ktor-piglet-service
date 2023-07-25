package com.example.db.dao.word

import bean.YDResponse
import com.example.db.DatabaseFactory
import com.example.net.NetworkClient
import com.example.toTextWithLine
import kotlinx.coroutines.delay
import models.Word
import models.Words
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DaoWordImpl: DaoWord {

    private fun resultRowToWord(resultRow: ResultRow): Word =
        Word(
            resultRow[Words.english],
            resultRow[Words.chinese],
            resultRow[Words.usPhonetic],
            resultRow[Words.ukPhonetic],
            resultRow[Words.usSpeech],
            resultRow[Words.ukSpeech]
        )

    //将英译汉返回的结果转换为word类
    private fun ydResponseToWord(ydResponse: YDResponse): Word =
        Word(
            ydResponse.query,
            ydResponse.basic.explains.toTextWithLine(),
            "/${ydResponse.basic.usPhonetic}/",
            "/${ydResponse.basic.ukPhonetic}/",
            ydResponse.basic.usSpeech,
            ydResponse.basic.ukSpeech
        )

    override suspend fun increaseWords(words: List<Word>): Boolean {
        return DatabaseFactory.dbQuery {
            try {
                Words.batchInsert(words){
                    this[Words.english] = it.english
                    this[Words.chinese] = it.chinese
                    this[Words.usPhonetic] = it.usPhonetic
                    this[Words.ukPhonetic] = it.ukPhonetic
                    this[Words.usSpeech] = it.usSpeech
                    this[Words.ukSpeech] = it.ukSpeech
                }.size == words.size
            }catch (e: Exception){
                false
            }
        }
    }

    override suspend fun deleteWords(englishList: List<String>): Boolean {
        return DatabaseFactory.dbQuery {
            try {
                englishList.forEach {
                    string -> Words.deleteWhere{ english eq string }
                }
                true
            }catch (e: Exception){
                false
            }
        }
    }

    override suspend fun queryWords(english: String, chinese: String): List<Word> {
        return DatabaseFactory.dbQuery {
            if (english.isEmpty() && chinese.isEmpty()) {
                Words.selectAll().orderBy(Words.datetime to SortOrder.DESC).map(::resultRowToWord)
            } else if (english.isNotEmpty()){
                val words = Words.select { Words.english eq english }.map(::resultRowToWord).toMutableList()
                if (words.isEmpty()) {
                    val searchFromYD = NetworkClient.searchFromYD(english, "en", "zh-CHS")
                    words.add(ydResponseToWord(searchFromYD))
                }
                words
            }else if (chinese.isNotEmpty()){
                val words = Words.select { Words.chinese like "%${chinese}%" }.map(::resultRowToWord).toMutableList()
                if (words.isEmpty()) {
                    val searchFromYD = NetworkClient.searchFromYD(chinese, "zh-CHS", "en")
                    searchFromYD.basic.explains.forEachIndexed { index, s ->
                        if (index >= 3 && index % 3 == 0) delay(1_000)
                        words.add(ydResponseToWord(NetworkClient.searchFromYD(s, "en", "zh-CHS")))
                    }
                }
                words
            }else{
                listOf<Word>()
            }
        }
    }
}

val wordDao = DaoWordImpl()