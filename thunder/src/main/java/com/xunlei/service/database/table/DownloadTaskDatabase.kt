package com.xunlei.service.database.table

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xunlei.service.database.bean.DownloadFileItemBean
import com.xunlei.service.database.bean.DownloadTaskBean
import com.xunlei.service.database.bean.MagnetTaskBean
import com.xunlei.service.database.dao.DownloadFileItemDao
import com.xunlei.service.database.dao.DownloadTaskDao
import com.xunlei.service.database.dao.MagnetTaskDao

/**
 * @author: read
 * @date: 2023/3/22 上午3:41
 * @description:
 */
@Database(entities = [DownloadTaskBean::class, DownloadFileItemBean::class, MagnetTaskBean::class], version = 2)
abstract class DownloadTaskDatabase : RoomDatabase() {
    abstract fun downloadTaskDao(): DownloadTaskDao

    abstract fun downloadFileItemDao(): DownloadFileItemDao

    abstract fun magnetTaskDao(): MagnetTaskDao
}