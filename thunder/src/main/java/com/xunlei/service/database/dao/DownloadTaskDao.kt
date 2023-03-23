package com.xunlei.service.database.dao

import androidx.room.*
import com.xunlei.service.database.bean.DownloadTaskBean

/**
 * @author: read
 * @date: 2023/3/22 上午3:33
 * @description:
 */
private const val tbName = "tb_torrent_download"
@Dao
interface DownloadTaskDao {

    @Query("SELECT * FROM $tbName")
    fun getAllTask(): List<DownloadTaskBean>

    @Query("SELECT * FROM $tbName WHERE temp_task_id=:tempTaskId")
    fun queryTaskByTempTaskId(tempTaskId: Long): DownloadTaskBean

    @Query("SELECT * FROM $tbName WHERE stable_task_id=:stableTaskId")
    fun queryTaskByStableTaskId(stableTaskId: String): DownloadTaskBean

    @Insert
    fun insertTask(taskBean: DownloadTaskBean)

    @Query("UPDATE $tbName SET temp_task_id = :tempTaskId WHERE stable_task_id = :stableTaskId")
    fun updateTaskTempIdByStableId(stableTaskId: String, tempTaskId: Long)

    @Query("UPDATE $tbName " +
            "SET " +
            "downloaded_size = :downloadedSize, " +
            "download_speed = :downloadSpeed," +
            "download_state = :downloadState, " +
            "is_finished = :isFinished " +
            "WHERE " +
            "stable_task_id = :stableTaskId ")
    fun updateTaskStatusByStableId(
        stableTaskId: String,
        downloadedSize: Long,
        downloadSpeed: Long,
        downloadState: Int,
        isFinished: Boolean
    )

    @Query("DELETE FROM $tbName WHERE temp_task_id=:tempTaskId")
    fun deleteTaskByTempId(tempTaskId: Long)

    @Query("DELETE FROM $tbName WHERE stable_task_id=:stableTaskId")
    fun deleteTaskByStableId(stableTaskId: String)

}