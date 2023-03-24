package com.thewind.torrent.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonNull
import com.thewind.downloader.HttpDownloader
import com.thewind.torrent.search.model.TorrentInfo
import com.thewind.torrent.search.model.TorrentSource
import com.thewind.torrent.search.service.TorrentServiceHelper
import com.xunlei.download.config.TORRENT_DIR
import com.xunlei.download.config.TorrentUtil
import com.xunlei.download.provider.TorrentTaskHelper
import com.xunlei.downloadlib.parameter.XLConstant.XLTaskStatus
import com.xunlei.service.database.TorrentDBHelper
import com.xunlei.util.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    fun downloadMagnetFile(src: Int, link: String, title: String, noParse: Boolean = true) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val hash = if (link.endsWith(".torrent")) {
                    handleHttpTorrent(link, title)
                } else {
                    handleMagnetTorrent(src, link, title)
                }
                waitSuccess(hash, noParse, 8)
            }
        }
    }

    private fun handleHttpTorrent(link: String, title: String): String {
        val hash = TorrentUtil.getMagnetHash(link)
        val fullPath = "$TORRENT_DIR$hash.torrent"
        var path = ""
        var file = File(fullPath)
        try {
            if (!file.exists() || file.length() == 0L) {
                HttpDownloader.download(link, fullPath)
            }
            file = File(fullPath)
            path = if (file.exists() && file.length() > 0) file.absolutePath else ""
            if (path.isNotEmpty()) {
                TorrentDBHelper.addMagnetTaskRecord(
                    hash = hash,
                    tempTaskId = hash.hashCode().toLong(),
                    magnetLink = link,
                    savePath = TorrentUtil.getLocalTorrentPath(hash),
                    title = title,
                    isFinished = true,
                    downloadState = XLTaskStatus.TASK_SUCCESS
                )
            }
        } catch (_: java.lang.Exception) {
        }
        return hash
    }

    private fun handleMagnetTorrent(
        src: Int, link: String, title: String
    ): String {
        val magnetLink = TorrentServiceHelper.requestMagnetLink(src = src, link = link).result
        val hash = TorrentUtil.getMagnetHash(magnetLink)
        val f = File(TorrentUtil.getLocalTorrentPath(hash))
        if (f.exists() && f.length() > 0) {
            TorrentDBHelper.addMagnetTaskRecord(
                hash = hash,
                tempTaskId = hash.hashCode().toLong(),
                magnetLink = link,
                savePath = TorrentUtil.getLocalTorrentPath(hash),
                title = title,
                isFinished = true,
                downloadState = XLTaskStatus.TASK_SUCCESS
            )
            return hash
        }
        if (hash.isNotEmpty()) {
            TorrentTaskHelper.instance.addMagnetTask(
                magnetLink = magnetLink,
                saveName = "${hash}.torrent",
                title = title
            )
        }
        return hash
    }

    private suspend fun waitSuccess(hash: String, noParse: Boolean, timeoutSecond: Int) {
        var curr = 0
        var validPath = ""
        while (curr < timeoutSecond) {
            val task = TorrentDBHelper.queryMagnetTaskByStableId(hash)
            Log.i(TAG, "waitSuccess, stableId = $hash, TASK = ${task.toJson()}")
            val path = task?.torrentPath ?: ""
            if (task?.isFinished == true) {
                validPath = path
                break
            }
            delay(1000)
            curr++
        }
        if (noParse) downTorrentLiveData.postValue(validPath) else parseMagnetFileLiveData.postValue(
            validPath
        )
    }

    fun parseOnlineMagnetFile(src: Int, link: String, title: String) {
        downloadMagnetFile(src, link, title, false)
    }

}
