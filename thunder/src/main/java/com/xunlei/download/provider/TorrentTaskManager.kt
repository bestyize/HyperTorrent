package com.xunlei.download.provider

import java.util.concurrent.ConcurrentHashMap


/**
 * @author : 亦泽
 * @date : 2023/3/14
 * @email : zhangrui10@bilibili.com
 */
class TorrentTaskManager {
    companion object {
        val instance by lazy { TorrentTaskManager() }
    }

    val taskRecords = ConcurrentHashMap<Long, Any>()

    val taskListener = ConcurrentHashMap<Long, TorrentTaskListener?>()

    @Synchronized
    fun registerTaskListener(taskId: Long, listener: TorrentTaskListener) {
        taskListener.put(taskId, listener)
    }

    @Synchronized
    fun removeTaskListener(taskId: Long) {
        taskListener.remove(taskId)
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
}


interface TorrentTaskListener {
    fun onStart(taskId: Long)
    fun onPause(taskId: Long)
    fun onStop(taskId: Long)
    fun onError(taskId: Long, errMsg: String)
    fun onCancel(taskId: Long)
    fun onSuccess(taskId: Long)
}