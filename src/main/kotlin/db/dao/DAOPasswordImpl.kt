package com.example.database.dao

import com.example.database.DatabaseFactory
import models.Password
import models.Passwords
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOPasswordImpl: DAOPassword {

    private fun resultRowToPassword(row: ResultRow) = Password(
        name = row[Passwords.name],
        content = row[Passwords.content],
        size = row[Passwords.size]
    )

    override suspend fun increasePassword(password: Password): Password? =
        DatabaseFactory.dbQuery {
            val selectResult = queryPassword(password.name)
            if (selectResult == null) {
                val insertStatement = Passwords.insert {
                    it[name] = password.name
                    it[content] = password.content
                    it[size] = password.size
                }
                insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToPassword)
            } else {
                null
            }
        }

    override suspend fun increasePassword(name: String, content: String, size: Int): Password? =
        DatabaseFactory.dbQuery {
            val selectResult = queryPassword(name)
            if (selectResult == null) {
                val insertStatement = Passwords.insert {
                    it[Passwords.name] = name
                    it[Passwords.content] = content
                    it[Passwords.size] = size
                }
                insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToPassword)
            } else {
                null
            }
        }

    override suspend fun queryPassword(name: String): Password?  =
        DatabaseFactory.dbQuery {
            Passwords.select {
                Passwords.name eq name
            }.map(::resultRowToPassword).singleOrNull()
        }

    override suspend fun allPasswords(): List<Password> =
        DatabaseFactory.dbQuery {
            Passwords.selectAll().map(::resultRowToPassword)
        }

    override suspend fun deletePassword(name: String): Boolean =
        DatabaseFactory.dbQuery {
            Passwords.deleteWhere{
                Passwords.name eq name
            } > 0
        }
}

val passwordDao: DAOPassword = DAOPasswordImpl()