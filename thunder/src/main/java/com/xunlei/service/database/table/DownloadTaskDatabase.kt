package com.xunlei.service.database.table

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xunlei.service.database.bean.DownloadTaskBean
import com.xunlei.service.database.dao.DownloadTaskDao

/**
 * @author: read
 * @date: 2023/3/22 上午3:41
 * @description:
 */
@Database(entities = [DownloadTaskBean::class], version = 1)
abstract class DownloadTaskDatabase : RoomDatabase() {
    abstract fun downloadTaskDao(): DownloadTaskDao
}