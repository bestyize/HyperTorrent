package com.xunlei.service.database.bean

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "tb_magnet_task")
class MagnetTaskBean {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "stable_task_id")
    var stableTaskId: String = ""

    @ColumnInfo(name = "temp_task_id")
    var tempTaskId: Long = 0

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "magnet_link")
    var magnetLink: String = ""

    @ColumnInfo(name = "download_state")
    var downloadState: Int = 0

    @ColumnInfo(name = "is_finished")
    var isFinished: Boolean = false

    @ColumnInfo(name = "torrent_path")
    var torrentPath: String = ""


}