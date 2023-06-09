package com.example.db.dao

import models.Password

interface DAOPassword {

    //添加密码
    suspend fun increasePassword(password: Password): Password?

    //删除密码
    suspend fun deletePassword(name: String): Boolean

    //删除所有密码
    suspend fun deleteAllPassword(): Boolean

    //更新密码
    suspend fun updatePassword(password: Password): Boolean

    //查询密码
    suspend fun queryPassword(name: String): Password?

    //查询全部密码
    suspend fun queryAllPassword(): List<Password>
}