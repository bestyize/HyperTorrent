package com.thewind.util

import android.net.Uri
import com.thewind.hyper.R
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: read
 * @date: 2023/3/17 上午12:52
 * @description:
 */
private const val KB = 1024L
private const val MB = 1024 * 1024L
private const val GB = 1024 * 1024 * 1024L

private val dateFormat = SimpleDateFormat("yyyy-MM-dd : HH:mm:ss", Locale.CHINA)

private val numberFormat = DecimalFormat("0.##")


fun Long.formatSize(): String {
    return when {
        this < KB -> "$this B"
        this < MB -> "${numberFormat.format(this.toDouble() / KB)} KB"
        this < GB -> "${numberFormat.format(this.toDouble() / MB)} MB"
        else -> "${numberFormat.format(this.toDouble() / GB)} GB"
    }
}


fun File.formatSize(): String {
    return if (isFile) length().formatSize() else ""
}

fun File.formatDate(): String {
    return dateFormat.format(lastModified())
}

fun String.postfix(): String {
    return substring(lastIndexOf(".") + 1)
}

fun String.urlFilePostfix(): String {
    val uri = Uri.parse(this)
    return if (this.endsWith("fm=png")) "png" else uri.path?.postfix()?:""
}

fun String.icon(): Int {
    return when {
        this.isVideo() -> R.drawable.youtube
        this.isAudio() -> R.drawable.audio
        this.isPicture() -> R.drawable.picture
        this.isCompress() -> R.drawable.zip
        this.isText() -> R.drawable.txt
        this.isDocument() -> R.drawable.document
        this.isTorrent() -> R.drawable.torrent
        this.isApk() -> R.drawable.android
        this.isJson() -> R.drawable.json
        this.isPdf() -> R.drawable.pdf
        else -> R.drawable.unknown
    }
}

fun File.icon(): Int {
    return when {
        this.isVideo() -> R.drawable.youtube
        this.isAudio() -> R.drawable.audio
        this.isPicture() -> R.drawable.picture
        this.isCompress() -> R.drawable.zip
        this.isText() -> R.drawable.txt
        this.isDocument() -> R.drawable.document
        this.isTorrent() -> R.drawable.torrent
        this.isDirectory -> R.drawable.folder
        this.isApk() -> R.drawable.android
        this.isJson() -> R.drawable.json
        this.isPdf() -> R.drawable.pdf
        else -> R.drawable.unknown
    }
}

fun String.isVideo(): Boolean {
    return when (postfix().lowercase()) {
        "mp4", "mkv", "mov", "avi", "flv", "mpeg", "mpg", "3gp", "wmv", "f4v", "rmvb", "rm", "asf" -> true
        else -> false
    }
}

fun File.isVideo(): Boolean {
    return extension.lowercase().isVideo()
}

fun String.isPicture(): Boolean {
    return when (postfix().lowercase()) {
        "jpg", "jpeg", "png", "webp", "gif", "svg", "bmp", "avif", "tif", "heif" -> true
        else -> false
    }
}

fun File.isPicture(): Boolean {
    return extension.isPicture()
}

fun String.isAudio(): Boolean {
    return when (postfix().lowercase()) {
        "mp3", "ape", "flac", "m4a", "aac", "wma", "wav" -> true
        else -> false
    }
}


fun File.isAudio(): Boolean {
    return extension.lowercase().isAudio()
}

fun String.isCompress(): Boolean {
    return when (postfix().lowercase()) {
        "zip", "rar", "7z", "gz", "bz", "bz2", "tgz", "xz", "taz", "tar" -> true
        else -> false
    }
}

fun File.isCompress(): Boolean {
    return extension.lowercase().isCompress()
}

fun String.isText(): Boolean {
    return when (postfix().lowercase()) {
        "txt" -> true
        else -> false
    }
}

fun File.isText(): Boolean {
    return extension.lowercase().isText()
}

fun String.isDocument(): Boolean {
    return when (postfix().lowercase()) {
        "doc", "docx", "ppt", "pptx", "xls", "md" -> true
        else -> false
    }
}

fun File.isDocument(): Boolean {
    return extension.isDocument()
}

fun String.isTorrent(): Boolean {
    return postfix() == "torrent"
}

fun String.isJson(): Boolean = postfix() == "json"

fun String.isApk(): Boolean {
    return postfix() == "apk"
}

fun File.isApk(): Boolean {
    return extension.isApk()
}

fun File.isTorrent(): Boolean {
    return isFile && extension.isTorrent()
}

fun File.isJson(): Boolean {
    return isFile && extension.isJson()
}

fun File.isPdf(): Boolean {
    return isFile && extension.isPdf()
}

fun File.isTextFile(): Boolean {
    return isFile && length() < 1024 * 50L && codeFormatList.contains(extension)
}

private fun String.isPdf(): Boolean = postfix() == "pdf"

fun MutableList<File>.nameSort(): MutableList<File> {
    val fileList = filter { it.isFile && !it.name.startsWith(".") }.sortedBy { it.name.lowercase() }
    val directoryList = filter { it.isDirectory && !it.name.startsWith(".") }.sortedBy { it.name.lowercase() }
    clear()
    addAll(directoryList)
    addAll(fileList)
    return this
}

fun MutableList<File>.nameSortReverse(): MutableList<File> {
    val fileList = filter { it.isFile }.sortedBy { it.name.lowercase() }.reversed()
    val directoryList = filter { it.isDirectory }.sortedBy { it.name.lowercase() }.reversed()
    clear()
    addAll(directoryList)
    addAll(fileList)
    return this
}

fun MutableList<File>.timeSort(): MutableList<File> {
    val fileList = filter { it.isFile }.sortedBy { it.lastModified() }
    val directoryList = filter { it.isDirectory }.sortedBy { it.lastModified() }
    clear()
    addAll(directoryList)
    addAll(fileList)
    return this
}

fun MutableList<File>.timeSortReverse(): MutableList<File> {
    val fileList = filter { it.isFile }.sortedBy { it.lastModified() }.reversed()
    val directoryList = filter { it.isDirectory }.sortedBy { it.lastModified() }.reversed()
    clear()
    addAll(directoryList)
    addAll(fileList)
    return this
}


private val codeFormatList = listOf(
    "java",
    "php",
    "jsp",
    "kt",
    "c",
    "cpp",
    "xml",
    "json",
    "txt",
    "ini",
    "conf",
    "js",
    "ts",
    "asm",
    "html",
    "c++",
    "rs",
    "h",
    "lua",
    "hpp",
    "cs",
    "py",
    "python",
    "go",
    "sql",
    "cmd",
    "sh",
    "htm",
    "css",
    "vue",
    "csv",
    "bat",
    "cmd",
    "gradle",
    "properties",
    "yaml",
    "configure",
    "makefile",
    "md"
)

