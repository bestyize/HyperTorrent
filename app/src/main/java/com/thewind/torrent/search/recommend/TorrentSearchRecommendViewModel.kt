package com.thewind.torrent.search.recommend

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tencent.mmkv.MMKV
import com.thewind.torrent.search.model.TorrentConfigResponse
import com.thewind.torrent.search.model.TorrentSource
import com.thewind.torrent.search.service.TorrentServiceHelper
import com.thewind.util.fromJson
import com.thewind.util.toJson
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
                val localResp = MMKV.defaultMMKV().decodeString(CFG_TORRENT_SEARCH_RECOMMEND_PAGE_KEY)?.fromJson<TorrentConfigResponse>()?: TorrentConfigResponse()
                sources.postValue(localResp.result)
                TorrentServiceHelper.requestTorrentConfig()
            }.let {
                MMKV.defaultMMKV().encode(CFG_TORRENT_SEARCH_RECOMMEND_PAGE_KEY, it.toJson())
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

    companion object {
        private const val CFG_TORRENT_SEARCH_RECOMMEND_PAGE_KEY = "cfg.torrent.search.recommend.tabs"
    }

}

class TorrentSearchOperator{
    var title: String = ""
    var keyword: String = ""
}