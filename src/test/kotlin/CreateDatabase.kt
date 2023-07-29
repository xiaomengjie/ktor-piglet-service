import models.Passwords
import models.Word
import models.Words
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import java.time.LocalDateTime

class CreateDatabase {
    @Test
    fun create(){
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./database/piglet_test"
        val database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            SchemaUtils.create(Passwords, Words)
        }
    }

    @Test
    fun select(){
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./database/piglet_test"
        val database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            val result = Words.selectAll().orderBy(Words.datetime to SortOrder.DESC)
            val dataByDay = result.map {
                Pair(
                    it[Words.datetime].toLocalDate(),
                    Word(it[Words.english], it[Words.chinese], it[Words.usPhonetic], it[Words.ukPhonetic], it[Words.usSpeech], it[Words.ukSpeech])
                )
            }.groupBy { it.first } // Grouping by the date (LocalDate)
            for ((date, dataList) in dataByDay) {
                println("Date: $date")
                for ((_, word) in dataList) {
                    println("Word: $word")
                }
            }
        }
    }
}