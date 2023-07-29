import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.nio.charset.StandardCharsets
import java.util.Base64

fun encrypt(text: String, key: String): String {
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    val secretKey = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), "AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val encryptedBytes = cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8))
    return Base64.getEncoder().encodeToString(encryptedBytes)
}

fun decrypt(encryptedText: String, key: String): String {
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    val secretKey = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), "AES")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText))
    return String(decryptedBytes, StandardCharsets.UTF_8)
}

fun main() {
    val plainText = "Hello, this is a secret message!"
    val encryptionKey = "ThisIsASecretKey"

    val encryptedText = encrypt(plainText, encryptionKey)
    println("Encrypted Text: $encryptedText")

    val decryptedText = decrypt(encryptedText, encryptionKey)
    println("Decrypted Text: $decryptedText")
}