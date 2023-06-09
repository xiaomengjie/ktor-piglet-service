package com.example.database.dao

import models.Password

interface DAOPassword {

    suspend fun increasePassword(password: Password): Password?

    suspend fun increasePassword(name: String, content: String, size: Int): Password?

    suspend fun queryPassword(name: String): Password?

    suspend fun allPasswords(): List<Password>

    suspend fun deletePassword(name: String): Boolean
}