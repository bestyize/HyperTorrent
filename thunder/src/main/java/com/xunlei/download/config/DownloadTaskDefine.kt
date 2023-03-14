package com.xunlei.download.config

import android.os.Environment
import java.io.File


/**
 * @author: read
 * @date: 2023/3/3 上午12:33
 * @description:
 */

val TORRENT_PREFIX = "magnet:?xt=urn:btih:"

val BASE_DIR by lazy { Environment.getExternalStorageDirectory().absolutePath + File.separator + "a.torrent" }
val BASE_DOWNLOAD_DIR by lazy { BASE_DIR + File.separator + "download" + File.separator}
val TORRENT_DIR by lazy { BASE_DOWNLOAD_DIR +  "torrent" + File.separator }
val TORRENT_FILE_DIR by lazy { BASE_DOWNLOAD_DIR + "files" + File.separator }