package com.xunlei.service.database.bean

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: read
 * @date: 2023/3/23 上午12:31
 * @description:
 */

@Keep
@Entity(tableName = "tb_torrent_download_file_item")
class DownloadFileItemBean {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo("temp_task_id")
    var tempTaskId: Long = 0
    @ColumnInfo("stable_task_id")
    var stableTaskId: String = ""
    @ColumnInfo("file_index")
    var index: Int = 0
    @ColumnInfo("file_name")
    var name: String ?= ""
    @ColumnInfo("file_size")
    var size: Long = 0L
    @ColumnInfo("downloaded_size")
    var downloadedSize: Long = 0L
    @ColumnInfo("download_speed")
    var downloadSpeed: Long = 0L
    @ColumnInfo("download_state")
    var downloadState: Int = 0
    @ColumnInfo("save_path")
    var savePath: String = ""
    @ColumnInfo("is_checked")
    var isChecked = true
}