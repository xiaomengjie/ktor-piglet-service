import bean.YDResponse
import com.example.net.NetworkClient
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

private val englishes = listOf("have", "be", "exist", "possess", "adsum")
private val words = mutableListOf<YDResponse>()

fun main() = runBlocking {
    val executionTime = measureTimeMillis {
        coroutineScope {
            englishes.forEach {
                val searchFromYD = NetworkClient.searchFromYD(it, "en", "zh-CHS")
                words.add(searchFromYD)
            }
        }
    }
    println("所有协程执行完成，总耗时：$executionTime 毫秒")
    words.forEach {
        println(it.query)
    }
}

suspend fun executeCoroutine(index: Int): String {
    delay((index + 1) * 1000L) // 模拟协程的执行时间
    return "协程${index + 1}执行完成"
}
