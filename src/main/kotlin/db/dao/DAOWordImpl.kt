package com.example.db.dao

import bean.YDResponse
import com.example.db.DatabaseFactory
import com.example.net.NetworkClient
import com.example.net.toText
import models.Word
import models.Words
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like

class DAOWordImpl: DAOWord {

    private fun resultRowToWord(resultRow: ResultRow): Word =
        Word(
            resultRow[Words.english],
            resultRow[Words.chinese],
            resultRow[Words.americaPronunciation],
            resultRow[Words.englandPronunciation]
        )

    //将英译汉返回的结果转换为word类
    private fun ydResponseToWord(ydResponse: YDResponse): Word =
        Word(ydResponse.query,
            ydResponse.basic.explains.toText(),
            "/${ydResponse.basic.usPhonetic}/",
            "/${ydResponse.basic.ukPhonetic}/"
        )

    override suspend fun increaseWord(word: Word): Word? =
        DatabaseFactory.dbQuery {
            val selectResult = queryWord(word.english, "english")
            if (selectResult.isEmpty()) {
                val insertStatement = Words.insert {
                    it[english] = word.english
                    it[chinese] = word.chinese
                    it[americaPronunciation] = word.americaPronunciation
                    it[englandPronunciation] = word.englandPronunciation
                }
                insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToWord)
            } else {
                if (updateWord(word)) word else null
            }
        }

    override suspend fun deleteWord(english: String): Boolean =
        DatabaseFactory.dbQuery {
            Words.deleteWhere(op = {Words.english eq english}) > 0
        }

    override suspend fun deleteAllWord(): Boolean =
        DatabaseFactory.dbQuery {
            Words.deleteAll() > 0
        }

    override suspend fun updateWord(word: Word): Boolean =
        DatabaseFactory.dbQuery {
            Words.update({Words.english eq word.english}){
                it[english] = word.english
                it[chinese] = word.chinese
                it[americaPronunciation] = word.americaPronunciation
                it[englandPronunciation] = word.englandPronunciation
            } > 0
        }

    override suspend fun queryWord(key: String, type: String): List<Word> =
        DatabaseFactory.dbQuery {
            val condition = when(type){
                "english" -> Words.english eq key
                "chinese" -> Words.chinese like "%${key}%"
                else -> return@dbQuery listOf<Word>()
            }
            //数据库查询
            val result = Words.select(condition).map(::resultRowToWord).toMutableList()

            //数据库没有查询到数据，则网络查询
            if (result.isEmpty()){
                val ydResponse = NetworkClient.searchFromYD(key,
                if (type == "english") "en" else "zh-CHS",
                if (type == "english") "zh-CHS" else "en")

                if (ydResponse.l == "en2zh-CHS"){
                    //英译汉时结果转换
                    result.add(ydResponseToWord(ydResponse))
                }else{
                    //汉译英时，将结果再次查询
                    ydResponse.basic.explains.forEach {
                        val response = NetworkClient.searchFromYD(it, "en", "zh-CHS")
                        result.add(ydResponseToWord(response))
                    }
                }
            }
            result
        }

    override suspend fun queryAllWord(): List<Word> =
        DatabaseFactory.dbQuery {
            Words.selectAll().orderBy(Words.datetime to SortOrder.DESC).map(::resultRowToWord)
        }
}

val wordDao = DAOWordImpl()