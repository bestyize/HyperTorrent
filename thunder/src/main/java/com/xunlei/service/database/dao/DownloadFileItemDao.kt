package com.xunlei.service.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.xunlei.service.database.bean.DownloadFileItemBean

/**
 * @author: read
 * @date: 2023/3/23 上午12:30
 * @description:
 */


private const val tbName = "tb_torrent_download_file_item"

@Dao
interface DownloadFileItemDao {

    @Insert
    fun insertDownloadItem(fileItem: DownloadFileItemBean)

    @Query("SELECT * FROM $tbName WHERE stable_task_id=:stableTaskId")
    fun queryDownloadItemsByStableTaskId(stableTaskId: String): List<DownloadFileItemBean>

    @Query("SELECT * FROM $tbName WHERE stable_task_id=:stableTaskId AND file_index = :fileIndex")
    fun queryDownloadItemsByStableTaskIdAndIndex(stableTaskId: String, fileIndex: Int): List<DownloadFileItemBean>

    @Query("SELECT * FROM $tbName WHERE temp_task_id=:tempTaskId")
    fun queryDownloadItemsByTempTaskId(tempTaskId: String): List<DownloadFileItemBean>

    @Query("UPDATE $tbName " +
            "SET " +
            "downloaded_size = :downloadedSize, " +
            "download_speed = :downloadSpeed," +
            "download_state = :downloadState " +
            "WHERE " +
            "stable_task_id = :stableTaskId " +
            "AND " +
            "file_index = :fileIndex")
    fun updateDownloadItemByStableIdAndIndex(
        stableTaskId: String,
        fileIndex: Int,
        downloadedSize: Long,
        downloadSpeed: Long,
        downloadState: Int
    )

    @Query("DELETE FROM $tbName WHERE stable_task_id = :stableTaskId")
    fun deleteTaskByStableTaskId(stableTaskId: String)
}