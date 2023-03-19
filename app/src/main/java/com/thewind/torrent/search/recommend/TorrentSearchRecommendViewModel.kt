package com.thewind.torrent.search.recommend

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.torrent.search.model.TorrentSource
import com.thewind.torrent.search.service.TorrentServiceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/3/18 下午11:31
 * @description:
 */
class TorrentSearchRecommendViewModel : ViewModel() {

    val sources: MutableLiveData<MutableList<TorrentSource>> = MutableLiveData()

    val searchOperatorLiveData: MutableLiveData<TorrentSearchOperator> = MutableLiveData()

    fun loadSource() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                TorrentServiceHelper.requestTorrentConfig()
            }.let {
                sources.value = it.result
            }
        }
    }

    fun notifySearch(title: String, keyword: String) {
        searchOperatorLiveData.value = TorrentSearchOperator().apply {
            this.title = title
            this.keyword = keyword
        }
    }

}

class TorrentSearchOperator{
    var title: String = ""
    var keyword: String = ""
}