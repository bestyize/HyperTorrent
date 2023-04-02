package com.thewind.community.feed.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.community.feed.model.RecommendFeetCard
import com.thewind.community.feed.service.RecommendFeedServiceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/3/30 上午1:07
 * @description:
 */
class RecommendFeedViewModel : ViewModel() {

    val cardListLiveData: MutableLiveData<List<RecommendFeetCard>> = MutableLiveData()

    fun loadCardList(channel: String, page: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                RecommendFeedServiceHelper.loadCards(channel, page).data
            }.let {
                cardListLiveData.value = it
            }
        }
    }

}