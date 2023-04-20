package com.thewind.downloader

import android.util.Log
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author: read
 * @date: 2023/3/19 上午3:37
 * @description:
 */
object HttpDownloader {

    fun download(urlString: String?, destination: String, extra: MutableMap<String, String> = mutableMapOf()) : Boolean {
        urlString ?: return false
        try {
            //user-agent -> Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36
            //cookie -> cf_clearance=PE9HYccupgUntVN9hddn1ulElgOzH1d..s9LgL_vDKI-1681927945-0-160
            val url = URL(urlString)
            val connection = url.openConnection()
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            extra.forEach { (t, u) ->  connection.setRequestProperty(t, u)}
            val inputStream = connection.getInputStream()
            val outputStream = FileOutputStream(destination)
            val buffer = ByteArray(1024)
            var bytesRead = 0
            while (bytesRead != -1) {
                bytesRead = inputStream.read(buffer)
                if (bytesRead != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
            }
            outputStream.close()
            inputStream.close()
            return true
        } catch (_: java.lang.Exception) {
            Log.e(TAG, "")
        }

        return false

    }

}

private const val TAG = "HttpDownloader"