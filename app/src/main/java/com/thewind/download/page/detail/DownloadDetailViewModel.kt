package com.thewind.download.page.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.download.page.model.DownloadDisplayItem
import kotlinx.coroutines.launch

/**
 * @author: read
 * @date: 2023/3/20 上午12:51
 * @description:
 */
class DownloadDetailViewModel : ViewModel() {

    val detailList: MutableLiveData<MutableList<DownloadDisplayItem>> = MutableLiveData()

    fun loadDetailList(taskId: String) {
        viewModelScope.launch {
            DownloadDetailService.queryDownloadState(taskId).let {
                detailList.value = it
            }
        }
    }

}


object DownloadDetailService {
    fun queryDownloadState(taskId: String): MutableList<DownloadDisplayItem> {
        return mutableListOf<DownloadDisplayItem>().apply {
            addAll(fakeData)
        }
    }

    private val fakeData = mutableListOf<DownloadDisplayItem>(
        DownloadDisplayItem().apply {
            this.fileName = "权力的游戏第一季第1集"
            this.downloadSpeed = 1024 * 1024
            this.downloadState = 1
            this.totalSize = 1024 * 1024 * 1024 * 2L
            this.downloadedSize = (this.totalSize * 0.8).toLong()
        },
        DownloadDisplayItem().apply {
            this.fileName = "权力的游戏第一季第2集"
            this.downloadSpeed = 1024 * 1024
            this.downloadState = 1
            this.totalSize = 1024 * 1024 * 1024 * 2L
            this.downloadedSize = (this.totalSize * 0.5).toLong()
        },
        DownloadDisplayItem().apply {
            this.fileName = "权力的游戏第一季第3集"
            this.downloadSpeed = 1024 * 1024 * 4
            this.downloadState = 1
            this.totalSize = 1024 * 1024 * 1024 * 2L
            this.downloadedSize = (this.totalSize * 0.0).toLong()
        }
    )
}