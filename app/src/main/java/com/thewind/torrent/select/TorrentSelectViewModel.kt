package com.thewind.torrent.select

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.util.toast
import com.xunlei.download.provider.TorrentTaskHelper
import com.xunlei.tool.editor.TorrentEditor
import com.xunlei.tool.editor.TorrentFileSimpleInfo
import com.xunlei.tool.editor.TorrentSimpleInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/3/20 上午1:25
 * @description:
 */
class TorrentSelectViewModel : ViewModel() {

    val torrentFileListLiveData: MutableLiveData<TorrentSimpleInfo> = MutableLiveData()

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

    fun loadMagnetInfo(path: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val simpleInfo = TorrentEditor.parseTorrentFile(path)
                simpleInfo.filesList = mutableListOf()
                val torrentInfo = TorrentTaskHelper.instance.getTorrentInfo(path)
                torrentInfo.mSubFileInfo?.forEach {
                    simpleInfo.filesList.add(TorrentFileSimpleInfo().apply {
                        this.name = it.mFileName
                        this.index = it.mFileIndex
                        this.size = it.mFileSize
                        this.isChecked = true

                    })
                }
                simpleInfo
            }.let {
                torrentFileListLiveData.value = it
            }
        }
    }

    companion object {
        private const val TAG = "TorrentSelectViewModel"
    }

}
