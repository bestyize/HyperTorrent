package com.thewind.torrent.select

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.util.toast
import com.xunlei.download.provider.TorrentTaskHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/3/20 上午1:25
 * @description:
 */
class TorrentSelectViewModel : ViewModel() {

    fun addMagnetTask(torrentFilePath: String, selectedList: MutableList<Int>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val torrentInfo = TorrentTaskHelper.instance.getTorrentInfo(torrentFilePath)
                val taskId = TorrentTaskHelper.instance.addTorrentTask(torrentInfo = torrentInfo, selectedFileList = selectedList)
                if (taskId < 1000) {
                    Log.e(TAG, "addTorrentTaskFailed, filePath = $torrentFilePath")
                } else {
                    toast("成功添加到下载列表")
                    Log.i(TAG, "addTorrentTaskSuccess, taskId = filePath = $torrentFilePath")
                }
            }

        }
    }

    companion object {
        private const val TAG = "TorrentSelectViewModel"
    }

}