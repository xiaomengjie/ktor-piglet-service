fun generateComplexPassword(length: Int): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', '<', '>', '?')
    return (1..length)
        .map { kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}

fun main() {
    val passwordLength = 12 // 密码长度，可以根据需要调整
    for (i in 1..10) {
        val password = generateComplexPassword(passwordLength)
        println("生成的复杂密码：$password")
    }
}
