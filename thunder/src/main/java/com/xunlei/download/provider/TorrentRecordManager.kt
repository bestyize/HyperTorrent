package com.xunlei.download.provider

import com.xunlei.download.config.TORRENT_DIR
import com.xunlei.util.TaskManager
import java.io.File
import java.util.concurrent.ConcurrentHashMap


/**
 * @author : 亦泽
 * @date : 2023/3/14
 * @email : zhangrui10@bilibili.com
 */
class TorrentRecordManager {
    companion object {
        private const val TAG = "[xunlei]TorrentTaskManager"
        val instance by lazy { TorrentRecordManager() }
    }

    fun init() {
        TaskManager.execute {
            while (true) {
                for (taskId in 1000..TorrentTaskHelper.instance.maxTaskId) {
                    val xlTaskInfo = TorrentTaskHelper.instance.getTaskInfo(taskId)
                    taskRecords.forEach { (t, u) ->
                        run {
                            if (u?.taskId == taskId) {
                                u.xlTaskInfo = xlTaskInfo
                                when (xlTaskInfo.mTaskStatus) {
                                    0 -> taskListener[u.magnetHash]?.onInit(u)
                                    1 -> taskListener[u.magnetHash]?.onDownloading(u)
                                    2 -> taskListener[u.magnetHash]?.onSuccess(u)
                                    3 -> taskListener[u.magnetHash]?.onError(u)
                                }
                            }
                        }
                    }

                }
                Thread.sleep(2000)
            }
        }
    }

    private val taskRecords = ConcurrentHashMap<String, TaskInfo?>()

    private val taskListener = ConcurrentHashMap<String, TorrentTaskListener?>()

    @Synchronized
    fun addTaskRecord(taskInfo: TaskInfo) {
        taskRecords[taskInfo.magnetHash] = taskInfo
    }

    @Synchronized
    fun initTaskRecord() {
        File(TORRENT_DIR).listFiles()?.forEach {
            if (!it.isFile || !it.name.endsWith(".torrent")) {
                return@forEach
            }
            val torrentInfo = TorrentTaskHelper.instance.getTorrentInfo(it.absolutePath)
            val hash = torrentInfo.mInfoHash
            if (hash != null && taskRecords[hash] == null) {

                val taskId = TorrentTaskHelper.instance.addTorrentTask(
                    torrentInfo = torrentInfo,
                    torrentFilePath = it.absolutePath,
                    selectedFileList = mutableListOf(),
                    autoStart = false
                )
                taskRecords[hash] = TaskInfo().apply {
                    this.taskId = taskId
                    this.torrentFilePath = it.absolutePath
                    this.magnetHash = hash
                    this.xlTaskInfo = TorrentTaskHelper.instance.getTaskInfo(taskId)
                }
            }
        }

    }

    @Synchronized
    fun registerTaskListener(magnetLink: String, listener: TorrentTaskListener) {
        taskListener[magnetLink] = listener
    }

    @Synchronized
    fun removeTaskListener(magnetLink: String) {
        taskListener.remove(magnetLink)
    }

    @Synchronized
    fun removeTaskListener(listener: TorrentTaskListener) {
        taskListener.contains(listener)
        taskListener.iterator().forEach {
            if (it.value == listener) {
                it.setValue(null)
            }
        }
    }

    @Synchronized
    fun getTorrentTaskList(): MutableList<TaskInfo> {
        return taskRecords.values.asSequence().filterNotNull().toMutableList()
    }
}


interface TorrentTaskListener {

    fun onInit(taskInfo: TaskInfo){}
    fun onStart(taskInfo: TaskInfo)

    fun onDownloading(taskInfo: TaskInfo)
    fun onPause(taskInfo: TaskInfo) {}
    fun onStop(taskInfo: TaskInfo) {}
    fun onError(taskInfo: TaskInfo)
    fun onCancel(taskInfo: TaskInfo) {}
    fun onSuccess(taskInfo: TaskInfo)
}