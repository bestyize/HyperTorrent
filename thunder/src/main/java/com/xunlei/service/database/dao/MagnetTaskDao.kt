package com.xunlei.service.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.xunlei.service.database.bean.MagnetTaskBean

private const val tbName = "tb_magnet_task"

@Dao
interface MagnetTaskDao {

    @Insert
    fun insertTask(magnetTaskBean: MagnetTaskBean)

    @Query("SELECT * FROM $tbName")
    fun getAllTask(): List<MagnetTaskBean>

    @Query("DELETE FROM $tbName")
    fun removeAllRecord()

    @Query("SELECT * FROM $tbName WHERE stable_task_id = :stableTaskId")
    fun queryMagnetTaskByStableId(stableTaskId: String): MagnetTaskBean?

    @Query("UPDATE $tbName SET temp_task_id = :tempTaskId WHERE stable_task_id = :stableTaskId")
    fun updateMagnetTaskTempIdByStableId(stableTaskId: String, tempTaskId: Long)

    @Query("DELETE FROM $tbName WHERE stable_task_id = :stableTaskId")
    fun deleteMagnetTaskByStableId(stableTaskId: String)

    @Query("UPDATE $tbName SET download_state = :downloadState, is_finished = :isFinished WHERE stable_task_id = :stableTaskId")
    fun updateTaskStatusByStableId(stableTaskId: String, downloadState: Int, isFinished: Boolean)

}