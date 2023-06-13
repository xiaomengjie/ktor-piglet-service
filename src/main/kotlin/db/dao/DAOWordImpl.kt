package com.example.db.dao

import com.example.db.DatabaseFactory
import models.Passwords
import models.Word
import models.Words
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOWordImpl: DAOWord {

    private fun resultRowToWord(resultRow: ResultRow): Word =
        Word(
            resultRow[Words.english],
            resultRow[Words.chinese],
            resultRow[Words.americaPronunciation],
            resultRow[Words.englandPronunciation]
        )

    override suspend fun increaseWord(word: Word): Word? =
        DatabaseFactory.dbQuery {
            val selectResult = queryWord(word.english, "english")
            if (selectResult.isEmpty()) {
                val insertStatement = Passwords.insert {
                    it[Words.english] = word.english
                    it[Words.chinese] = word.chinese
                    it[Words.americaPronunciation] = word.americaPronunciation
                    it[Words.englandPronunciation] = word.englandPronunciation
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
            Passwords.update({Words.english eq word.english}){
                it[Words.english] = word.english
                it[Words.chinese] = word.chinese
                it[Words.americaPronunciation] = word.americaPronunciation
                it[Words.englandPronunciation] = word.englandPronunciation
            } > 0
        }

    override suspend fun queryWord(key: String, type: String): List<Word> =
        DatabaseFactory.dbQuery {
            val condition = when(type){
                "english" -> Words.english eq key
                "chinese" -> Words.chinese eq key
                else -> return@dbQuery listOf<Word>()
            }
            Words.select(condition).map(::resultRowToWord)
        }

    override suspend fun queryAllWord(): List<Word> =
        DatabaseFactory.dbQuery {
            Words.selectAll().map(::resultRowToWord)
        }
}

val wordDao = DAOWordImpl()