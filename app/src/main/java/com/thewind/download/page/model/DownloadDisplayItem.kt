package com.thewind.download.page.model

/**
 * @author: read
 * @date: 2023/3/20 上午12:27
 * @description:
 */
class DownloadDisplayItem {
    var fileName: String = ""
    var downloadedSize: Long = 0L
    var totalSize: Long = 0L
    var downloadState: Int = 0
    var downloadSpeed: Long = 0L
}