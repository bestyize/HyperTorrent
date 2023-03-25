package com.thewind.download.page.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xunlei.service.database.TorrentDBHelper
import com.xunlei.service.database.bean.DownloadTaskBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/3/20 上午12:51
 * @description:
 */
class DownloadDetailViewModel : ViewModel() {

    val downloadTaskLiveData: MutableLiveData<DownloadTaskBean> = MutableLiveData()

    fun loadDetailList(stableTaskId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                DownloadDetailService.queryDownloadState(stableTaskId)
            }.let {
                downloadTaskLiveData.value = it
            }

        }
    }

}


object DownloadDetailService {
    fun queryDownloadState(stableId: String): DownloadTaskBean {
        return TorrentDBHelper.queryDownloadTaskByStableId(stableId)
    }

}