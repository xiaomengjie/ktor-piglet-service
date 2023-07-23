package com.example.db.dao

import models.Word

interface DAOWord {

    //添加单词
    suspend fun increaseWord(word: Word): Word?

    //删除单词
    suspend fun deleteWord(english: String): Boolean

    //删除所有单词
    suspend fun deleteAllWord(): Boolean

    //更新单词
    suspend fun updateWord(word: Word): Boolean

    //查询单词
    suspend fun queryWord(key: String, type: String): List<Word>

    //查询全部单词
    suspend fun queryAllWord(): List<Word>
}