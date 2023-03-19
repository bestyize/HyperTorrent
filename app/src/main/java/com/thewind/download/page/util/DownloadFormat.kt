package com.thewind.download.page.util

import com.thewind.util.formatSize

/**
 * @author: read
 * @date: 2023/3/20 上午12:37
 * @description:
 */
object DownloadFormat {
    fun formatDownloadState(state: Int): String {
        return when(state) {
            0 -> "暂停中"
            1 -> "下载中"
            2 -> "下载完成"
            3 -> "敏感资源"
            else -> "下载错误"
        }
    }

    fun formatDownloadSpeed(speed: Long): String {
        return speed.formatSize() + "/s"
    }

    fun formatDownloadProgress(downloadedSize: Long, totalSize: Long): Int {
        if (totalSize == 0L) return 100
        val progress = 100 * (downloadedSize.toDouble() / totalSize.toDouble())
        return progress.toInt()
    }
}