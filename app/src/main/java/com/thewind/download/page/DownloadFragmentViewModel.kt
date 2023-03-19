package com.thewind.download.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.download.page.model.DownloadDisplayItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/3/20 上午12:12
 * @description:
 */
class DownloadFragmentViewModel :ViewModel() {

    val downloadTaskListLiveDate: MutableLiveData<MutableList<DownloadDisplayItem>> = MutableLiveData()

    fun loadDownloadRecord() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                DownloadFragmentService.queryDownloadTaskRecord()
            }.let {
                downloadTaskListLiveDate.value = it
            }
        }
    }

}


object DownloadFragmentService {


    fun queryDownloadTaskRecord(): MutableList<DownloadDisplayItem> {
        return mutableListOf()
    }

}