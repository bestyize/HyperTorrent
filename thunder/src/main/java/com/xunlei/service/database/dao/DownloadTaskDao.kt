package com.xunlei.service.database.dao

import androidx.room.*
import com.xunlei.service.database.bean.DownloadTaskBean

/**
 * @author: read
 * @date: 2023/3/22 上午3:33
 * @description:
 */
private const val dbName = "tb_torrent_download"
@Dao
interface DownloadTaskDao {

    @Query("SELECT * FROM $dbName")
    fun getAllTask(): List<DownloadTaskBean>

    @Query("SELECT * FROM $dbName WHERE temp_task_id=:tempTaskId")
    fun getTaskByTempTaskId(tempTaskId: Long): DownloadTaskBean

    @Insert
    fun addTask(taskBean: DownloadTaskBean)

    @Query("UPDATE $dbName SET")
    fun updateTaskByTaskId(task: DownloadTaskBean)

    @Query("DELETE FROM $dbName WHERE temp_task_id=:tempTaskId")
    fun deleteTaskByTempId(tempTaskId: Long)



}