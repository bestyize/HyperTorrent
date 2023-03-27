package com.thewind.util

import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

/**
 * @author: read
 * @date: 2023/3/28 上午12:15
 * @description:
 */
object EncryptUtil {
    private val PUBLIC_KEY =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyQbtc2nuoqM2dLjcalkxpdQXz/GJHMyV30N1z/EWATCzC88ETYQu5mpvzGJkziEbXrR4SGGVatAPo/Zd15FdyosJlP7aIWGWQEmWjgdPIJmQh6SefGgd7nyERAiWj3bq9RMdkBgf0bxuXHAK8EqvonWxrbrlH9yDZbKzrzpjBggt6ywE5hhqq+VcnPnBmUwF5xg+QRO9JIdHcogyV15CCq7hMb/ZTuBlxD75Iob9OwuJ+PvanxUfFs5BFkizXMjezQ+ZsjueZNaAYge9RFLns5lCkXmXDL+BiC49cYeSsI2z5FDQyLCmeJLuPm7wCcmX17mWL5A5i41h8ZbLxcGV6QIDAQAB"

    private const val PRIVATE_KEY =
        "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDJBu1zae6iozZ0uNxqWTGl1BfP8YkczJXfQ3XP8RYBMLMLzwRNhC7mam/MYmTOIRtetHhIYZVq0A+j9l3XkV3KiwmU/tohYZZASZaOB08gmZCHpJ58aB3ufIRECJaPdur1Ex2QGB/RvG5ccArwSq+idbGtuuUf3INlsrOvOmMGCC3rLATmGGqr5Vyc+cGZTAXnGD5BE70kh0dyiDJXXkIKruExv9lO4GXEPvkihv07C4n4+9qfFR8WzkEWSLNcyN7ND5myO55k1oBiB71EUuezmUKReZcMv4GILj1xh5KwjbPkUNDIsKZ4ku4+bvAJyZfXuZYvkDmLjWHxlsvFwZXpAgMBAAECggEAI5jTcU1/0VLfcLnPZhGXRgsRkGPv6Kadn2Rdt020nM9X1S56T5OpjmmolGOUACBCrXl9lyuYfsqLCni001hNaHFIaBbc325dYdLmEw5eR6KJwks3OUZijnmEHlv69TqUjoMtB/gNomEglcPNXBGNmlIG/YGL0yGELvz+7neDU2zM5Ulax8P3mweW9txYAtDsi3s1jWe/mDnf9qK9NcPkWgecyhm0hUqh5z/V2Btl1GOtYhxgwzi1Lcm2ZONNCLiVUe/EUQu6zp/lV93ZRaN7w+tQ8vgpx9crCD/ysnYNL6BAk6DwAjpks6gm2LBMjTdZR29RpvXlrU3a2FZfBzxmlQKBgQDWjmrudocNqmpG2dNpMwJ9YC+kwklA30Ipi3CoXYKikNh/xOhg0Bv1npzgKWVxReW4t/AgI/zomO+RVijDJyAaao0UJlER9baWtWG4gwex8xi1Xaxbstf4D06gYxzDs1yAq7wfwxg3ootOa/LDmnOgs7cDvWyCPRrs53tYisl9kwKBgQDv24CbOyj96FQmc41uln0UC0r2Q4hZ5wsvOuyopktfozjFDVA14C2zldr8oMcPaiPQpB6/GaAiaBl+Nrke8/8mq5dGJtxeGfWrbYg8OU15In0MCbWzLZaPPhXhhRShSfiziyNWADnjPiGv/syefjTBRrfrwii7Rc9j8GzdDugsEwKBgQCsSOYCn/iod4wpYmZJliVMqbz4Lu6FggDtvUN0Xso0Yd0HFAWJH/Mjkv7jXTpQ79CcOE9Ab8V4vL26VZYbxsIXlDG+2VnF8ylv8SwL7mNyeAMCN7rcU0rqrNf1IEELwG3qAHzqgE1k90enbDUe7lok08qE3UlRd2G0k9hO6/sJswKBgA1JDTCL8FNQAQk4MUdE0py/m2FUkELah0YQb3dP7rDXa5eOizjQt/dQf5aqmRwDdwRhcsqG8tW1CmRxR4OtZB6SNojOMjqMGY1rPbaIPgBNprt59jR8e/BnMfMR2fitDjdzp3tl+87YbClBZbgJqYZjEmDemj71OO2CLaV44+c9AoGBAMkZ3/CrdxHF5NSVSeyuw/fMj+MwznYIMXb2D3A9SNgwOYwKyhDMlNBR0dnL7B4w3ppOFqxvsHpNqFiFjdbFC26jCTIFD5Se2pvz6pClKN0dLeZRb/aDtQcT5k0XaP2uLSj9yCT5+Dzgi9+4MOwqDT3jZ4JMU7HZt8L3eIDPh38O"


    fun encrypt(message: String): String {
        try {
            // Convert the Base64-encoded public key to a PublicKey object
            val publicKeyBytes: ByteArray = Base64.getDecoder().decode(PUBLIC_KEY)
            val publicKeySpec = X509EncodedKeySpec(publicKeyBytes)
            val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
            val publicKey: PublicKey = keyFactory.generatePublic(publicKeySpec)

            // Create a cipher instance and initialize it with the public key
            val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)

            // Encrypt the message and return it in Base64 format
            val encryptedMessageBytes: ByteArray = cipher.doFinal(message.toByteArray())
            return Base64.getEncoder().encodeToString(encryptedMessageBytes)
        } catch (_: Exception) {
        }
        return ""
    }

    fun decrypt(encryptedMessage: String?): String {
        try {
            // Convert the Base64-encoded private key to a PrivateKey object
            val privateKeyBytes = Base64.getDecoder().decode(PRIVATE_KEY)
            val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            val privateKey: PrivateKey = keyFactory.generatePrivate(privateKeySpec)

            // Create a cipher instance and initialize it with the private key
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.DECRYPT_MODE, privateKey)

            // Decrypt the message and return it as a string
            val encryptedMessageBytes = Base64.getDecoder().decode(encryptedMessage)
            val decryptedMessageBytes = cipher.doFinal(encryptedMessageBytes)
            return String(decryptedMessageBytes)
        } catch (e: java.lang.Exception) {
        }
        return ""
    }
}