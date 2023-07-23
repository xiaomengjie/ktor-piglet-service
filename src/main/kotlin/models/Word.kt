package models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

@Serializable
data class Word(
    val english: String,
    val chinese: String,
    val americaPronunciation: String,
    val englandPronunciation: String
)

object Words: Table(){
    val id = integer("id").autoIncrement()
    val english = varchar("english", 64)
    val chinese = varchar("chinese", 512)
    val americaPronunciation = varchar("america_pronunciation", 64)
    val englandPronunciation = varchar("england_pronunciation", 64)
    val datetime = datetime("datetime").defaultExpression(CurrentDateTime)
}