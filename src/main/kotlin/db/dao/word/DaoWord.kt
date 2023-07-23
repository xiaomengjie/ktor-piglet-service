package com.example.db.dao.word

import models.Word

interface DaoWord {

    //添加单词
    suspend fun increaseWords(words: List<Word>): Boolean

    //删除单词
    suspend fun deleteWords(englishList: List<String>): Boolean

    //查询单词（都为empty时查询全部）
    suspend fun queryWords(english: String = "", chinese: String = ""): List<Word>
}