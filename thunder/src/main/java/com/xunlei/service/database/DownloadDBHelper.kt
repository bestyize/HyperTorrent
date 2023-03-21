package com.xunlei.service.database

import androidx.room.Room
import com.xunlei.downloadlib.XLDownloadManager
import com.xunlei.service.database.table.DownloadTaskDatabase

/**
 * @author: read
 * @date: 2023/3/22 上午3:50
 * @description:
 */


val downloadDb by lazy {
    Room.databaseBuilder(XLDownloadManager.getInstance().context, DownloadTaskDatabase::class.java, "download_db").build()
}