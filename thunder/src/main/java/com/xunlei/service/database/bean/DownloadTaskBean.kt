package com.xunlei.service.database.bean

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * @author: read
 * @date: 2023/3/22 上午3:11
 * @description:
 */
@Keep
@Entity
class DownloadTaskBean {
    @PrimaryKey
    var id: Int = 0 //自增key
    @ColumnInfo(name = "task_first_create_time")
    var taskFirstCreateTime: Long = 0L // 数据库首次添加的时间
    @ColumnInfo(name = "temp_task_id")
    var tempTaskId: Int = 0 // 一次启动中生成的taskId
    @ColumnInfo(name = "torrent_path")
    var torrentPath: String = ""
    @ColumnInfo(name = "sub_file_list")
    var subFileList = mutableListOf<TorrentDownloadTaskFileInfo>()

}

@Keep
class TorrentDownloadTaskFileInfo {
    var name: String ?= ""
    var size: Long = 0L
    var downloadedSize: Long = 0L
    var downloadSpeed: Long = 0L
    var downloadState: Int = 0
    var savePath: String = ""
    var index: Int = 0
    var isChecked = true
}