package com.xunlei.service.database.bean

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * @author: read
 * @date: 2023/3/22 上午3:11
 * @description:
 */
@Keep
@Entity(tableName = "tb_torrent_download")
class DownloadTaskBean {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0 //自增key

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "task_first_create_time")
    var taskFirstCreateTime: Long = 0L // 数据库首次添加的时间

    @ColumnInfo(name = "total_size")
    var totalSize: Long = 0L

    // xunlei sdk给的task_id
    @ColumnInfo(name = "temp_task_id")
    var tempTaskId: Long = 0

    // 永久ID 格式 hash
    @ColumnInfo("stable_task_id")
    var stableTaskId: String = ""

    // 种子文件存储地址
    @ColumnInfo(name = "torrent_path")
    var torrentPath: String = ""

    @ColumnInfo("download_state")
    var downloadState: Int = 0

    @ColumnInfo("downloaded_size")
    var downloadedSize: Long = 0L

    @ColumnInfo("file_size")
    var size: Long = 0L


    @ColumnInfo("download_speed")
    var downloadSpeed: Long = 0L

    @ColumnInfo("is_finished")
    var isFinished = false


    @Ignore
    var fileItemList = mutableListOf<DownloadFileItemBean>()

}
