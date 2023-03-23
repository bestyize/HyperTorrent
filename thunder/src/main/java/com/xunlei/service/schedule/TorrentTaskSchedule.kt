package com.xunlei.service.schedule

import android.util.Log
import com.xunlei.download.provider.TorrentTaskHelper
import com.xunlei.service.database.TorrentDBHelper
import com.xunlei.service.database.bean.DownloadTaskBean
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
                delay(200)
                while (true) {
                    Log.i(TAG, "start, before query all")
                    val downloadTaskList = TorrentDBHelper.queryAllDownloadTask()
                    Log.i(TAG, "start, after query all, and start update record")
                    downloadTaskList.forEach { task ->
                        task.fileItemList.filter { it.isChecked && !it.isFinished }.forEach {
                            val subTaskInfo =
                                TorrentTaskHelper.instance.getSubTaskInfo(it.tempTaskId, it.index)

                            var needUpdate = false
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
                                    it.downloadedSize == it.size
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
                                task.downloadState == 2
                            )
                        }
                    }
                    Log.i(TAG, "start, end update record")
                    delay(2000)
                }
            }
        }
    }

    private fun restoreDownloadTask() {
        MainScope().launch {
            launch(Dispatchers.IO) {
                Log.i(TAG, "restoreDownloadTask, start")
                val downloadTaskList = TorrentDBHelper.queryAllDownloadTask()
                downloadTaskList.filter { !it.isFinished }.forEach { task ->
                    restoreSingleTask(task)
                }
                Log.i(TAG, "restoreDownloadTask, finished, restore task.size = ${downloadTaskList.size}")
            }
        }
    }

    @Synchronized
    fun restoreSingleTask(task: DownloadTaskBean) {
        val torrentInfo = TorrentTaskHelper.instance.getTorrentInfo(task.torrentPath)
        val selectedList =
            task.fileItemList.filter { it.isChecked }.map { it.index }.toMutableList()
        task.tempTaskId = TorrentTaskHelper.instance.addTorrentTask(
            torrentInfo = torrentInfo,
            selectedFileList = selectedList,
            autoStart = false,
            addToDatabase = false
        )
        TorrentDBHelper.updateTaskTempIdByStableId(task.stableTaskId, task.tempTaskId)
    }

}

private const val TAG = "TorrentTaskSchedule"