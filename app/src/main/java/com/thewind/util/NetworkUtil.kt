package com.thewind.util

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


/**
 * @author: read
 * @date: 2023/3/7 上午12:12
 * @description:
 */

fun get(link: String? = null): String {
    val conn = URL(link).openConnection() as HttpURLConnection

    conn.requestMethod = "GET"
    conn.readTimeout = 5000
    conn.connectTimeout = 5000

    val reader = BufferedReader(InputStreamReader(conn.inputStream))
    val sb = java.lang.StringBuilder()
    var line: String?

    do {
        line = reader.readLine()
        if (line != null) {
            sb.append(line)
        }
    } while (line != null)
    reader.close()
    conn.disconnect()
    return sb.toString()
}