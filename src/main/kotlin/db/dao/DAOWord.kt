package com.example.database.dao

import models.Word

interface DAOWord {

    suspend fun increaseWord(word: Word): Word?

    suspend fun queryWord(key: String, type: String): List<Word>?

    suspend fun queryAllWord(): List<Word>

    suspend fun editWord(word: Word): Boolean
}