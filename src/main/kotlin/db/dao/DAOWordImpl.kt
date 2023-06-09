package com.example.database.dao

import com.example.database.DatabaseFactory
import models.Word
import models.Words
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOWordImpl: DAOWord {

    private fun resultRowToWord(resultRow: ResultRow): Word =
        Word(
            resultRow[Words.english], resultRow[Words.chinese], resultRow[Words.pronunciation]
        )

    override suspend fun increaseWord(word: Word): Word?  = DatabaseFactory.dbQuery {
        val queryResult = queryWord(word.english, "english").singleOrNull()
        if (queryResult == null){
            Words.insert {
                it[english] = word.english
                it[chinese] = word.chinese
                it[pronunciation] = word.pronunciation
            }.resultedValues?.singleOrNull()?.let(::resultRowToWord)
        }else{
            if (editWord(word)){
                word
            }else{
                null
            }
        }
    }

    override suspend fun queryWord(key: String, type: String): List<Word> = DatabaseFactory.dbQuery {
        val condition = when(type){
            "english" -> Words.english eq key
            "chinese" -> Words.chinese eq key
            else -> return@dbQuery listOf<Word>()
        }
        Words.select(condition).map(::resultRowToWord)
    }

    override suspend fun queryAllWord(): List<Word> = DatabaseFactory.dbQuery {
        Words.selectAll().map(::resultRowToWord)
    }

    override suspend fun editWord(word: Word): Boolean = DatabaseFactory.dbQuery {
        Words.update({Words.english eq word.english}){
            it[english] = word.english
            it[chinese] = word.chinese
            it[pronunciation] = word.pronunciation
        } > 0
    }
}

val wordDao = DAOWordImpl()