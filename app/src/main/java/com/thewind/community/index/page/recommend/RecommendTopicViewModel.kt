package com.thewind.community.index.page.recommend

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.community.index.model.RecommendTopicItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/4/7 上午1:58
 * @description:
 */
class RecommendTopicViewModel: ViewModel() {

    val recommendTopicLiveData: MutableLiveData<RecommendTopicItem> = MutableLiveData()

    fun loadRecommendTopic() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

            }
        }
    }

}