package models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Password(
    val name: String,
    val content: String,
    val size: Int
)

object Passwords: Table(){
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val content = varchar("content", 128)
    val size = integer("size")
}