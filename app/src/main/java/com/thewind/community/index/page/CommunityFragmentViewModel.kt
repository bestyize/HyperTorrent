package com.thewind.community.index.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.community.index.model.RecommendTopicItem
import com.thewind.community.index.service.RecommendTopicServiceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/4/7 上午1:07
 * @description:
 */
class CommunityFragmentViewModel : ViewModel() {

    val recommendTopicLiveData: MutableLiveData<List<RecommendTopicItem>> = MutableLiveData()

    fun loadRecommendTopic() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                RecommendTopicServiceHelper.loadRecommendTopicList()
            }.let {
                recommendTopicLiveData.value = it
            }
        }
    }
}