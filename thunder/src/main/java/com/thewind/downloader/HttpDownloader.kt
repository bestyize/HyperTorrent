package com.thewind.downloader

import java.io.*
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author: read
 * @date: 2023/3/19 上午3:37
 * @description:
 */
object HttpDownloader {

    fun download(urlString: String, destination: String) : Boolean {
        try {
            val url = URL(urlString)
            val connection = url.openConnection()
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
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

        }

        return false

    }

}