package com.xunlei.service.schedule

import android.util.Log
import com.xunlei.download.provider.TorrentTaskHelper
import com.xunlei.downloadlib.XLDownloadManager
import com.xunlei.downloadlib.parameter.BtIndexSet
import com.xunlei.downloadlib.parameter.XLConstant.XLTaskStatus
import com.xunlei.service.database.TorrentDBHelper
import com.xunlei.service.database.bean.DownloadTaskBean
import com.xunlei.util.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author: read
 * @date: 2023/3/23 下午11:46
 * @description:
 */
class TorrentTaskSchedule {

    private var isStart = false

    companion object {
        val INSTANCE by lazy {
            TorrentTaskSchedule()
        }
    }

    fun startSchedule() {
        if (isStart) return
        isStart = true
        MainScope().launch {
            launch(Dispatchers.IO) {
                restoreDownloadTask()
                clearMagnetTask()
                delay(200)
                while (true) {
                    handleTorrentTask()
                    handleMagnetTask()
                    delay(1000)
                }
            }
        }
    }

    private fun handleTorrentTask() {
        Log.i(TAG, "handleTorrentTask, start")
        val downloadTaskList = TorrentDBHelper.queryAllDownloadTask()
        downloadTaskList.forEach { task ->
            task.fileItemList.filter { it.isChecked && !it.isFinished }.forEach {
                val subTaskInfo =
                    TorrentTaskHelper.instance.getSubTaskInfo(it.tempTaskId, it.index)
                if (!subTaskInfo.mIsSelect) {
                    val list = task.fileItemList.filter { it.isChecked }.map { it.index }.toMutableList()
                    val btIndexSet = BtIndexSet()
                    btIndexSet.mIndexSet = IntArray(list.size)
                    list.forEachIndexed { index, i -> btIndexSet.mIndexSet[index] = i }
                    XLDownloadManager.getInstance().selectBtSubTask(it.tempTaskId, btIndexSet)

                }
                if (it.isFinished) {
                    subTaskInfo.mTaskInfo.mTaskStatus = XLTaskStatus.TASK_SUCCESS
                }
                var needUpdate = false
                if (it.size != subTaskInfo.mTaskInfo.mFileSize) {
                    it.size = subTaskInfo.mTaskInfo.mFileSize
                    needUpdate = true
                }
                if (it.downloadedSize != subTaskInfo.mTaskInfo.mDownloadSize) {
                    it.downloadedSize = subTaskInfo.mTaskInfo.mDownloadSize
                    needUpdate = true
                }
                if (it.downloadState != subTaskInfo.mTaskInfo.mTaskStatus) {
                    it.downloadState = subTaskInfo.mTaskInfo.mTaskStatus
                    needUpdate = true
                }
                if (it.downloadSpeed != subTaskInfo.mTaskInfo.mDownloadSpeed) {
                    it.downloadSpeed = subTaskInfo.mTaskInfo.mDownloadSpeed
                    needUpdate = true
                }

                if (needUpdate) {
                    TorrentDBHelper.updateSingleFileItemState(
                        it.stableTaskId,
                        it.index,
                        it.downloadedSize,
                        it.downloadSpeed,
                        it.downloadState,
                        it.downloadedSize > 0 && it.downloadedSize == it.size
                    )
                }
            }
            val taskInfo = TorrentTaskHelper.instance.getTaskInfo(task.tempTaskId)
            task.size = taskInfo.mFileSize
            var needUpdate = false
            if (task.downloadedSize != taskInfo.mDownloadSize) {
                task.downloadedSize = taskInfo.mDownloadSize
                needUpdate = true
            }
            if (task.downloadState != taskInfo.mTaskStatus) {
                task.downloadState = taskInfo.mTaskStatus
                needUpdate = true
            }
            if (task.downloadSpeed != taskInfo.mDownloadSpeed) {
                task.downloadSpeed = taskInfo.mDownloadSpeed
                needUpdate = true
            }

            if (needUpdate) {
                TorrentDBHelper.updateTaskStatusByStableId(
                    task.stableTaskId,
                    task.downloadedSize,
                    task.downloadSpeed,
                    task.downloadState,
                    task.downloadState == XLTaskStatus.TASK_SUCCESS
                )
            }
        }
        Log.i(TAG, "handleTorrentTask, end")
    }

    private fun restoreDownloadTask() {
        Log.i(TAG, "restoreDownloadTask, start")
        val downloadTaskList = TorrentDBHelper.queryAllDownloadTask()
        downloadTaskList.filter { !it.isFinished }.forEach { task ->
            restoreSingleTask(task)
        }
        Log.i(TAG, "restoreDownloadTask, finished, restore task.size = ${downloadTaskList.size}")

    }

    @Synchronized
    fun restoreSingleTask(task: DownloadTaskBean) {
        val torrentInfo = TorrentTaskHelper.instance.getTorrentInfo(task.torrentPath)
        if (torrentInfo.mInfoHash.isNullOrBlank()) {
            TorrentDBHelper.removeDownloadTaskRecord(task.stableTaskId)
            return
        }
        val selectedList =
            task.fileItemList.filter { it.isChecked }.map { it.index }.toMutableList()
        task.tempTaskId = TorrentTaskHelper.instance.addTorrentTask(
            torrentInfo = torrentInfo,
            selectedFileList = selectedList,
            torrentFilePath = task.torrentPath,
            autoStart = false,
            addToDatabase = false
        )
        TorrentDBHelper.updateTaskTempIdByStableId(task.stableTaskId, task.tempTaskId)
    }

    private fun handleMagnetTask() {
        Log.i(TAG, "handleMagnetTask, start")
        val magnetTaskList = TorrentDBHelper.queryAllMagnetTask()
        magnetTaskList.filter { !it.isFinished }.forEach { task ->
            val taskInfo = TorrentTaskHelper.instance.getTaskInfo(task.tempTaskId)
            var isNeedUpdate = false
            if (task.downloadState != taskInfo.mTaskStatus) {
                task.downloadState = taskInfo.mTaskStatus
                isNeedUpdate = true
            }
            val isFinished = taskInfo.mTaskStatus == XLTaskStatus.TASK_SUCCESS
            if (task.isFinished != isFinished) {
                task.isFinished = isFinished
                isNeedUpdate = true
            }
            Log.i(TAG, "handleMagnetTask, taskStatus = ${taskInfo.mTaskStatus}, taskId = ${task.tempTaskId} taskInfo = ${task.toJson()}")
            if (isNeedUpdate) {
                TorrentDBHelper.updateMagnetTaskStatusByStableId(
                    task.stableTaskId,
                    task.downloadState,
                    task.isFinished)
            }
        }
        Log.i(TAG, "handleMagnetTask, end")
    }

    private fun clearMagnetTask() {
        TorrentDBHelper.removeAllMagnetTask()
    }

    private fun restoreMagnetTask() {
        Log.i(TAG, "restoreMagnetTask, start")
        val magnetTaskList = TorrentDBHelper.queryAllMagnetTask()
        magnetTaskList.filter { !it.isFinished }.forEach { task ->
            task.tempTaskId = TorrentTaskHelper.instance.addMagnetTask(
                magnetLink = task.magnetLink,
                saveDir = task.torrentPath,
                title = task.title,
                autoStart = false,
                addToDatabase = false)
            TorrentDBHelper.updateMagnetTaskTempIdByStableId(task.stableTaskId, tempTaskId = task.tempTaskId)
        }
        Log.i(TAG, "restoreMagnetTask, end")
    }

}

private const val TAG = "TorrentTaskSchedule"