package com.thewind.util

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.Proxy
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * @author: read
 * @date: 2023/3/20 上午2:37
 * @description:
 */
private const val TAG: String = "HttpUtil"

fun get(link: String?, headerMap: Map<String, String> = HashMap()): String {
    if (link == null || !link.startsWith("http")) {
        return ""
    }
    val sb = StringBuilder()
    try {
        val url = URL(link)
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.readTimeout = 5000
        conn.connectTimeout = 5000
        for (key in headerMap.keys) {
            conn.addRequestProperty(key, headerMap[key])
        }
        val reader = BufferedReader(InputStreamReader(conn.inputStream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            sb.append(line)
        }
        reader.close()
        conn.disconnect()
    } catch (e: Exception) {
        Log.e(TAG, "get, error , url = $link, errMsg = $e")
    }
    return sb.toString()
}

/**
 * 做POST请求
 *
 * @param link      请求地址
 * @param params    请求体，类似于keyword=十年&num=100这样的格式
 * @param headerMap 请求头
 * @return 请求结果
 */
fun post(link: String?, params: String?, headerMap: Map<String?, String?>?= null): String {
    if (link == null || !link.startsWith("http")) {
        return ""
    }
    var response: String = ""
    try {
        val url = URL(link)
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.addRequestProperty("Content-Type", "text/plain")
        conn.doInput = true
        conn.doOutput = true
        conn.connectTimeout = 10000
        conn.readTimeout = 10000
        if (headerMap != null && headerMap.keys.isNotEmpty()) {
            for (key in headerMap.keys) {
                conn.setRequestProperty(key, headerMap[key])
            }
        }
        val writer = PrintWriter(conn.outputStream)
        writer.print(params)
        writer.flush()
        val reader =
            BufferedReader(InputStreamReader(conn.inputStream, StandardCharsets.UTF_8))
        var line: String?
        val sb = StringBuilder()
        while (reader.readLine().also { line = it } != null) {
            sb.append(line)
        }
        writer.close()
        reader.close()
        conn.disconnect()
        response = sb.toString()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return response
}

fun urlToKv(link: String): Map<String?, String?>? {
    var url = link
    val map: MutableMap<String?, String?> = HashMap()
    if (url.isEmpty() == true) return map
    var start = url.indexOf("?")
    if (start == -1) {
        start = 0
    }
    url = url.substring(start)
    val pairs = url.split("&".toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray()
    for (s in pairs) {
        val pair = s.split("=".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        if (pair.size == 2) {
            map[pair[0]] = pair[1]
        }
    }
    return map
}