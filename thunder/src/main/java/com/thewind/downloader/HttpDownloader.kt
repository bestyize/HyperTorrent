package com.thewind.downloader

import android.util.Log
import java.io.FileOutputStream
import java.net.URL

/**
 * @author: read
 * @date: 2023/3/19 上午3:37
 * @description:
 */
object HttpDownloader {

    fun download(
        urlString: String?,
        destination: String,
        extra: MutableMap<String, String> = mutableMapOf()
    ): Boolean {
        urlString ?: return false
        try {
            val url = URL(urlString)
            val connection = url.openConnection()
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            extra.forEach { (t, u) -> connection.setRequestProperty(t, u) }
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