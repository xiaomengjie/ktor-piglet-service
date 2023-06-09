package models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Word(
    val english: String,
    val chinese: String,
    val pronunciation: String
)

object Words: Table(){
    val id = integer("id").autoIncrement()
    val english = varchar("english", 64)
    val chinese = varchar("chinese", 64)
    val pronunciation = varchar("pronunciation", 64)
}