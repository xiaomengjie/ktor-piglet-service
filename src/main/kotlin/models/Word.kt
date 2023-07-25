package models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

@Serializable
data class Word(
    val english: String,
    val chinese: String,
    val usPhonetic: String,
    val ukPhonetic: String,
    val usSpeech: String,
    val ukSpeech: String
)

object Words: Table(){
    val id = integer("id").autoIncrement()
    //唯一索引
    val datetime = datetime("datetime").defaultExpression(CurrentDateTime)
    //英文中文
    val english = varchar("english", 64).uniqueIndex()
    val chinese = varchar("chinese", 512)
    //美式英标、发音地址
    val usPhonetic = varchar("us_phonetic", 64)
    val usSpeech = varchar("us_speech", 512)
    //英式英标、发音地址
    val ukPhonetic = varchar("uk_phonetic", 64)
    val ukSpeech = varchar("uk_speech", 512)
}