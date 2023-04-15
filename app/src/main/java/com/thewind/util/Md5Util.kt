package com.thewind.util

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


/**
 * @author: read
 * @date: 2023/4/16 上午2:21
 * @description:
 */
object Md5Util {
    fun convertToMd5(plainText: String?): String {
        plainText ?: return ""
        var secretBytes: ByteArray? = null
        secretBytes = try {
            MessageDigest.getInstance("md5").digest(plainText.toByteArray(StandardCharsets.UTF_8))
        } catch (e: Exception) {
            return ""
        }
        var md5code = BigInteger(1, secretBytes).toString(16)
        for (i in 0 until 32 - md5code.length) {
            md5code = "0$md5code"
        }
        return md5code
    }

}