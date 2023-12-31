package com.example.db.dao.password

import com.example.aesDecrypt
import com.example.aesEncrypt
import com.example.db.DatabaseFactory
import models.Password
import models.Passwords
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DaoPasswordImpl: DaoPassword {

    private fun resultRowToPassword(row: ResultRow) = Password(
        name = row[Passwords.name],
        content = row[Passwords.content],
        size = row[Passwords.size]
    )

    private fun resultRowToEncryptPassword(row: ResultRow) = Password(
        name = row[Passwords.name],
        content = row[Passwords.content].aesEncrypt(),
        size = row[Passwords.size]
    )

    override suspend fun increasePassword(password: Password): Boolean =
        DatabaseFactory.dbQuery {
            val selectResult = queryPassword(password.name)
            if (selectResult == null) {
                val insertStatement = Passwords.insert {
                    it[name] = password.name
                    it[content] = password.content.aesDecrypt()
                    it[size] = password.size
                }
                insertStatement.resultedValues?.singleOrNull() != null
            } else {
                updatePassword(password)
            }
        }

    override suspend fun deletePassword(name: String): Boolean =
        DatabaseFactory.dbQuery {
            Passwords.deleteWhere{
                Passwords.name eq name
            } > 0
        }

    override suspend fun deleteAllPassword(): Boolean =
        DatabaseFactory.dbQuery {
            Passwords.deleteAll() > 0
        }

    override suspend fun updatePassword(password: Password): Boolean =
        DatabaseFactory.dbQuery {
            Passwords.update({Passwords.name eq password.name}){
                it[name] = password.name
                it[content] = password.content.aesDecrypt()
                it[size] = password.size
            } > 0
        }

    override suspend fun queryPassword(name: String): Password?  =
        DatabaseFactory.dbQuery {
            Passwords.select {
                Passwords.name eq name
            }.map(::resultRowToEncryptPassword).singleOrNull()
        }

    override suspend fun queryAllPassword(): List<Password> =
        DatabaseFactory.dbQuery {
            Passwords.selectAll().map(::resultRowToEncryptPassword)
        }
}

val passwordDao: DaoPassword = DaoPasswordImpl()