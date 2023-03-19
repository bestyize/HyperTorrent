package com.xunlei.download.provider

import android.util.Log
import com.xunlei.download.config.TORRENT_DIR
import com.xunlei.download.config.TORRENT_FILE_DIR
import com.xunlei.download.config.TORRENT_PREFIX
import com.xunlei.download.config.TorrentUtil
import com.xunlei.downloadlib.XLDownloadManager
import com.xunlei.downloadlib.parameter.*
import com.xunlei.util.TaskManager
import java.io.File
import kotlin.math.max


/**
 * @author: read
 * @date: 2023/3/3 上午12:26
 * @description:
 */
class TorrentTaskHelper private constructor() {

    companion object {
        val instance by lazy { TorrentTaskHelper() }
    }

    var maxTaskId: Long = -1

    @Synchronized
    fun addMagnetTask(
        magnetLink: String?,
        saveDir: String? = TORRENT_DIR,
        saveName: String? = null
    ): Long {
        val hash = TorrentUtil.getMagnetHash(magnetLink)
        if (hash.isEmpty()) {
            Log.i(TAG, "downloadMagnet, error magnet link is null or empty")
            return -1
        }
        var saveTorrentFileName = saveName
        if (saveName.isNullOrBlank()) {
            saveTorrentFileName = "$hash.torrent"
        }
        val magnet =
            if (magnetLink?.startsWith(TORRENT_PREFIX) == true) magnetLink else TORRENT_PREFIX + magnetLink
        val magnetTaskParam = MagnetTaskParam().apply {
            setFileName(saveTorrentFileName)
            setFilePath(saveDir)
            setUrl(magnet)
        }

        val task = GetTaskId()
        XLDownloadManager.getInstance().createBtMagnetTask(magnetTaskParam, task)
        XLDownloadManager.getInstance().setTaskLxState(task.taskId, 0, 1)
        val taskState = XLDownloadManager.getInstance().startTask(task.taskId)
        Log.i(TAG, "downloadMagnet, taskState = $taskState, taskId = ${task.taskId}")
        maxTaskId = if (maxTaskId == -1L) {
            task.taskId
        } else {
            max(task.taskId, maxTaskId)
        }
        return task.taskId
    }

    @Synchronized
    fun getTorrentInfo(torrentFilePath: String): TorrentInfo {
        val torrentInfo = TorrentInfo()
        val taskState = XLDownloadManager.getInstance().getTorrentInfo(torrentFilePath, torrentInfo)
        Log.i(TAG, "getTorrentInfo, taskState = $taskState")
        return torrentInfo
    }

    /**
     *  继续下载会重新创建一个taskId
     * @param torrentInfo TorrentInfo
     * @param saveDir String?
     * @param torrentFilePath String?
     * @param selectedFileList MutableList<Int>
     * @return Long
     */
    @Synchronized
    fun addTorrentTask(
        torrentInfo: TorrentInfo,
        saveDir: String? = TORRENT_FILE_DIR,
        torrentFilePath: String? = null,
        selectedFileList: MutableList<Int> = mutableListOf(),
        autoStart: Boolean = true
    ): Long {
        val fullPath = torrentFilePath
            ?: (TORRENT_DIR + TorrentUtil.getMagnetHash(torrentInfo.mInfoHash) + ".torrent")
        var z: Boolean
        val btTaskParam = BtTaskParam().apply {
            setCreateMode(1)
            setFilePath(saveDir)
            setMaxConcurrent(3)
            setSeqId(0)
            setTorrentPath(fullPath)
        }

        val task = GetTaskId()
        XLDownloadManager.getInstance().createBtTask(btTaskParam, task)
        if (torrentInfo.mSubFileInfo.isNotEmpty() && selectedFileList.isNotEmpty()) {
            val arrayList = ArrayList<Any>()
            for (torrentFileInfo in torrentInfo.mSubFileInfo) {
                val length = selectedFileList.size
                var i = 0
                while (true) {
                    if (i >= length) {
                        z = false
                        break
                    } else if (selectedFileList[i] == torrentFileInfo.mFileIndex) {
                        z = true
                        break
                    } else {
                        i++
                    }
                }
                if (!z) {
                    arrayList.add(Integer.valueOf(torrentFileInfo.mFileIndex))
                }
            }
            val btIndexSet = BtIndexSet(arrayList.size)
            for (i2 in 0 until arrayList.size) {
                btIndexSet.mIndexSet[i2] = (arrayList[i2] as Int).toInt()
            }
            XLDownloadManager.getInstance().deselectBtSubTask(task.taskId, btIndexSet)
        }
        XLDownloadManager.getInstance().setTaskLxState(task.taskId, 0, 1)
        if (autoStart) {
            val taskState = XLDownloadManager.getInstance().startTask(task.taskId)
            Log.i(TAG, "addTorrentTask, taskId = ${task.taskId}, taskState = $taskState")
        }
        maxTaskId = if (maxTaskId == -1L) {
            task.taskId
        } else {
            max(task.taskId, maxTaskId)
        }
        TorrentRecordManager.instance.addTaskRecord(TaskInfo().apply {
            this.taskType = TaskType.TORRENT
            this.taskId = task.taskId
            this.magnetHash = torrentInfo.mInfoHash
            this.torrentFilePath = fullPath
            this.xlTaskInfo = getTaskInfo(task.taskId)
        })
        return task.taskId
    }

    @Synchronized
    fun getTaskInfo(taskId: Long): XLTaskInfo {
        val xlTaskInfo = XLTaskInfo()
        val taskState = XLDownloadManager.getInstance().getTaskInfo(taskId, 1, xlTaskInfo)
        Log.i(
            TAG,
            "getTaskInfo, taskId = $taskId, taskState = ${
                XLDownloadManager.getInstance().getErrorCodeMsg(taskState)
            }"
        )
        return xlTaskInfo
    }

    @Synchronized
    fun getSubTaskInfo(taskId: Long, subTaskId: Int): BtSubTaskDetail {
        val subTask = BtSubTaskDetail()
        val taskState = XLDownloadManager.getInstance().getBtSubTaskInfo(taskId, subTaskId, subTask)
        Log.i(
            TAG,
            "getSubTaskInfo, taskId = $taskId, subTaskId = $subTaskId,taskState = $taskState"
        )
        return subTask
    }

    @Synchronized
    fun startTask(taskId: Long): Int {
        val taskState = XLDownloadManager.getInstance().startTask(taskId)
        Log.i(
            TAG,
            "getSubTaskInfo, taskId = $taskId,taskState = ${
                XLDownloadManager.getInstance().getErrorCodeMsg(taskState)
            }"
        )
        return taskState
    }

    @Synchronized
    fun stopTask(taskId: Long): Int {
        XLDownloadManager.getInstance().stopTask(taskId)
        val taskState = XLDownloadManager.getInstance().releaseTask(taskId)
        Log.i(
            TAG,
            "stopTask, taskState = ${XLDownloadManager.getInstance().getErrorCodeMsg(taskState)}"
        )
        return taskState
    }

    @Synchronized
    fun deleteTask(taskId: Long, filePath: String): Int {
        val taskState = stopTask(taskId)
        TaskManager.execute {
            val file = File(filePath)
            if (file.exists()) {
                file.delete()
            }
        }
        return taskState
    }

    @Synchronized
    fun pauseTask(taskId: Long): Int {
        return stopTask(taskId)
    }


}

private const val TAG = "[xunlei]TorrentTaskHelper"