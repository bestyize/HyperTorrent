package com.xunlei.download.provider

import android.util.Log
import com.xunlei.download.config.TORRENT_DIR
import com.xunlei.download.config.TORRENT_FILE_DIR
import com.xunlei.download.config.TORRENT_PREFIX
import com.xunlei.download.config.TorrentTaskHelper
import com.xunlei.downloadlib.XLDownloadManager
import com.xunlei.downloadlib.parameter.*
import com.xunlei.util.TaskManager
import java.io.File


/**
 * @author: read
 * @date: 2023/3/3 上午12:26
 * @description:
 */
class DownloadTaskManager private constructor() {

    companion object {
        val instance by lazy { DownloadTaskManager() }
    }

    @Synchronized
    fun addMagnetTask(
        magnetLink: String?,
        saveDir: String? = TORRENT_DIR,
        saveName: String? = null
    ): Long {
        val hash = TorrentTaskHelper.getMagnetHash(magnetLink)
        if (hash.isEmpty()) {
            Log.i(TAG, "downloadMagnet, error magnet link is null or empty")
            return -1
        }
        var saveTorrentFileName = saveName
        if (saveName.isNullOrBlank()) {
            saveTorrentFileName = "$hash.torrent"
        }
        val magnet = if (magnetLink?.startsWith(TORRENT_PREFIX) == true) magnetLink else TORRENT_PREFIX + magnetLink
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
        return task.taskId
    }

    @Synchronized
    fun getTorrentInfo(torrentFilePath: String): TorrentInfo {
        val torrentInfo = TorrentInfo()
        val taskState = XLDownloadManager.getInstance().getTorrentInfo(torrentFilePath, torrentInfo)
        Log.i(TAG, "getTorrentInfo, taskState = $taskState")
        return torrentInfo
    }

    @Synchronized
    fun addTorrentTask(
        torrentInfo: TorrentInfo,
        saveDir: String? = TORRENT_FILE_DIR,
        torrentFilePath: String ?= null,
        selectedFileList: MutableList<Int> = mutableListOf()
    ): Long {
        if (selectedFileList.isEmpty()) {
            torrentInfo.mSubFileInfo?.forEach {
                selectedFileList.add(it.mFileIndex)
            }
        }
        val fullPath = torrentFilePath
            ?: (TORRENT_DIR + TorrentTaskHelper.getMagnetHash(torrentInfo.mInfoHash) + ".torrent")
        var z: Boolean
        val btTaskParam = BtTaskParam().apply {
            setCreateMode(1)
            setFilePath(saveDir)
            setMaxConcurrent(3)
            setSeqId(0)
            setTorrentPath(fullPath)
        }

        val getTaskId = GetTaskId()
        XLDownloadManager.getInstance().createBtTask(btTaskParam, getTaskId)
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
            XLDownloadManager.getInstance().deselectBtSubTask(getTaskId.taskId, btIndexSet)
        }
        XLDownloadManager.getInstance().setTaskLxState(getTaskId.taskId, 0, 1)
        val taskState = XLDownloadManager.getInstance().startTask(getTaskId.taskId)
        Log.i(TAG, "addTorrentTask, taskId = ${getTaskId.taskId}, taskState = $taskState")
        return getTaskId.taskId
    }

    @Synchronized
    fun getTaskInfo(taskId: Long): XLTaskInfo {
        val xlTaskInfo = XLTaskInfo()
        val taskState = XLDownloadManager.getInstance().getTaskInfo(taskId, 1, xlTaskInfo)
        Log.i(TAG, "getTaskInfo, taskId = $taskId, taskState = $taskState")
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
    fun stopTask(taskId: Long) {
        XLDownloadManager.getInstance().stopTask(taskId)
        val taskState = XLDownloadManager.getInstance().releaseTask(taskId)
        Log.i(TAG, "stopTask, taskState = $taskState")
    }

    @Synchronized
    fun deleteTask(taskId: Long, filePath: String) {
        stopTask(taskId)
        TaskManager.execute {
            val file = File(filePath)
            if (file.exists()) {
                file.delete()
            }
        }
    }


}

private const val TAG = "DownloadTaskManager"