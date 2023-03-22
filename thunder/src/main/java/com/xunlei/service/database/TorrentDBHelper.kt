package com.xunlei.service.database

import android.util.Log
import androidx.room.Room
import com.xunlei.downloadlib.XLDownloadManager
import com.xunlei.service.database.bean.DownloadFileItemBean
import com.xunlei.service.database.bean.DownloadTaskBean
import com.xunlei.service.database.table.DownloadTaskDatabase
import com.xunlei.tool.editor.TorrentEditor
import com.xunlei.util.toJson
import java.io.File

/**
 * @author: read
 * @date: 2023/3/22 上午3:50
 * @description:
 */


private val downloadDb by lazy {
    Room.databaseBuilder(
        XLDownloadManager.getInstance().context,
        DownloadTaskDatabase::class.java,
        "torrent_download_db"
    ).build()
}

private const val TAG = "TorrentDBHelper"

object TorrentDBHelper {
    fun addDownloadTaskRecord(
        tempTaskId: Long,
        torrentPath: String,
        saveDir: String,
        selectedList: List<Int>
    ) {
        val info = TorrentEditor.parseTorrentFileWithThunder(torrentPath, selectedList)
        downloadDb.downloadTaskDao().insertTask(DownloadTaskBean().apply {
            this.tempTaskId = tempTaskId
            this.stableTaskId = info.hash
            this.taskFirstCreateTime = System.currentTimeMillis()
            this.torrentPath = torrentPath
            this.title = info.torrentTitle
        })
        for (item in info.filesList) {
            addDownloadFileItemRecord(
                tempTaskId,
                info.hash,
                item.name ?: "",
                item.size,
                item.index,
                saveDir + item.subPath + item.name,
                item.isChecked
            )
        }
        Log.i(TAG, "addDownloadTaskRecord , record = ${queryDownloadTaskByStableId(info.hash).toJson()}")
    }

    fun addDownloadFileItemRecord(
        tempTaskId: Long,
        hash: String,
        fileName: String,
        fileSize: Long,
        index: Int,
        savePath: String,
        isChecked: Boolean
    ) {
        val downloadedSize = File(savePath).let {
            if (it.exists() && it.isFile) it.length() else 0L
        }
        downloadDb.downloadFileItemDao().insertDownloadItem(DownloadFileItemBean().apply {
            this.stableTaskId = hash
            this.tempTaskId = tempTaskId
            this.savePath = savePath
            this.index = index
            this.name = fileName
            this.size = fileSize
            this.isChecked = isChecked
            this.downloadedSize = downloadedSize
        })
    }

    fun removeDownloadTaskRecord(stableId: String) {
        downloadDb.downloadTaskDao().deleteTaskByStableId(stableId)
        downloadDb.downloadFileItemDao().deleteTaskByStableTaskId(stableId)
    }


    fun queryDownloadTaskByStableId(stableId: String): DownloadTaskBean {
        return downloadDb.downloadTaskDao().queryTaskByStableTaskId(stableTaskId = stableId).apply {
            val files = downloadDb.downloadFileItemDao().queryDownloadItemsByStableTaskId(stableId)
            this.fileItemList.addAll(files)
        }
    }

    fun querySingleFileItemInfo(stableId: String, fileIndex: Int): DownloadFileItemBean {
        return downloadDb.downloadFileItemDao().queryDownloadItemsByStableTaskId(stableId)
            .first { it.index == fileIndex }
    }

    fun updateSingleFileItemState(
        stableTaskId: String,
        fileIndex: Int,
        downloadedSize: Long = 0L,
        downloadSpeed: Long = 0,
        downloadState: Int = 0
    ) {
        downloadDb.downloadFileItemDao().updateDownloadItemByStableIdAndIndex(
            stableTaskId,
            fileIndex,
            downloadedSize,
            downloadSpeed,
            downloadState,
        )
    }
}