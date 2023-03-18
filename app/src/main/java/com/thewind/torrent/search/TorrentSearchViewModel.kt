package com.thewind.torrent.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.torrent.search.model.TorrentInfo
import com.thewind.torrent.search.model.TorrentSource
import com.thewind.torrent.search.service.TorrentServiceHelper
import com.xunlei.download.config.TorrentUtil
import com.xunlei.download.provider.TaskInfo
import com.xunlei.download.provider.TorrentRecordManager
import com.xunlei.download.provider.TorrentTaskHelper
import com.xunlei.download.provider.TorrentTaskListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * @author: read
 * @date: 2023/3/18 下午5:12
 * @description:
 */
class TorrentSearchViewModel : ViewModel() {

    companion object {
        private val TAG = "TorrentSearchViewModel"
    }

    val results: MutableLiveData<MutableList<TorrentInfo>> = MutableLiveData()
    val magnetUrlLiveData: MutableLiveData<String> = MutableLiveData()
    val config: MutableLiveData<MutableList<TorrentSource>> = MutableLiveData()
    val downTorrentLiveData: MutableLiveData<String> = MutableLiveData()
    val parseMagnetFileLiveData: MutableLiveData<String> = MutableLiveData()


    fun search(keyword: String, src: Int, page: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                TorrentServiceHelper.search(keyword, src, page)
            }.let {
                results.value = it.list
            }
        }
    }

    fun requestMagnetLink(src: Int, link: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                TorrentServiceHelper.requestMagnetLink(src = src, link = link)
            }.let {
                magnetUrlLiveData.value = it.result
            }
        }
    }

    fun downloadMagnetFile(src: Int, link: String, noParse: Boolean = true) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val magnetLink =
                    TorrentServiceHelper.requestMagnetLink(src = src, link = link).result
                val hash = TorrentUtil.getMagnetHash(magnetLink)
                val f = File(TorrentUtil.getLocalTorrentPath(hash))
                if (f.exists() && f.length() > 0) {
                    TorrentRecordManager.instance.removeTaskListener(hash)
                    if (noParse) downTorrentLiveData.postValue(f.absolutePath) else parseMagnetFileLiveData.postValue(
                        f.absolutePath
                    )
                    return@withContext
                }
                if (hash.isNotEmpty()) {
                    val taskId = TorrentTaskHelper.instance.addMagnetTask(
                        magnetLink = magnetLink,
                        saveName = "${hash}.torrent"
                    )
                    if (taskId >= 1000) {
                        TorrentRecordManager.instance.addTaskRecord(TaskInfo().apply {
                            this.magnetHash = hash
                            this.taskId = taskId
                        })
                    }

                    TorrentRecordManager.instance.registerTaskListener(hash, object :
                        TorrentTaskListener {

                        override fun onInit(taskInfo: TaskInfo) {
                            val filePath = TorrentUtil.getLocalTorrentPath(hash)
                            val file = File(filePath)
                            if (file.exists() && file.length() > 0) {
                                TorrentRecordManager.instance.removeTaskListener(hash)
                                if (noParse) downTorrentLiveData.postValue(file.absolutePath) else parseMagnetFileLiveData.postValue(
                                    filePath
                                )
                            }
                        }

                        override fun onStart(taskInfo: TaskInfo) {
                            Log.i(TAG, "downloadMagnetFile, onStart")
                        }

                        override fun onDownloading(taskInfo: TaskInfo) {

                        }

                        override fun onError(taskInfo: TaskInfo) {
                            TorrentRecordManager.instance.removeTaskListener(hash)
                            if (noParse) downTorrentLiveData.postValue("") else parseMagnetFileLiveData.postValue(
                                ""
                            )
                        }

                        override fun onSuccess(taskInfo: TaskInfo) {
                            Log.i(TAG, "downloadMagnetFile, onSuccess")
                            TorrentRecordManager.instance.removeTaskListener(hash)
                            if (noParse) downTorrentLiveData.postValue(
                                TorrentUtil.getLocalTorrentPath(
                                    hash
                                )
                            ) else parseMagnetFileLiveData.postValue(
                                TorrentUtil.getLocalTorrentPath(
                                    hash
                                )
                            )
                        }
                    })
                } else {
                    TorrentRecordManager.instance.removeTaskListener(hash)
                    if (noParse) downTorrentLiveData.postValue("") else parseMagnetFileLiveData.postValue(
                        ""
                    )
                }

            }
        }
    }

    fun parseOnlineMagnetFile(src: Int, link: String) {
        downloadMagnetFile(src, link, false)
    }

    fun requestTorrentConfig() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                TorrentServiceHelper.requestTorrentConfig()
            }.let {
                config.value = it.result
            }
        }
    }

}


object TorrentSearchService {

    fun requestTabs(): MutableList<TorrentSource> {
        return mutableListOf(TorrentSource().apply {
            src = 0
            title = "BtSow"
            desc = "Torrent"
            officialUrl = "https://btsow.com"
        })
    }
}