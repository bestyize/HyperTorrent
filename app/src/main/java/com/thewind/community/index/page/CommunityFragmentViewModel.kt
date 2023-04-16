package com.thewind.community.index.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tencent.mmkv.MMKV
import com.thewind.community.index.service.RecommendTopicServiceHelper
import com.thewind.community.topic.model.TopicCardData
import com.thewind.community.topic.model.TopicListResponse
import com.thewind.util.fromJson
import com.thewind.util.toJson
import com.thewind.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * @author: read
 * @date: 2023/4/7 上午1:07
 * @description:
 */
class CommunityFragmentViewModel : ViewModel() {

    val recommendTopicLiveData: MutableLiveData<TopicCardData> = MutableLiveData()

    fun loadRecommendTopic() {
        val localResp = MMKV.defaultMMKV().getString("recommend_topic_response", "") ?: ""
        if (localResp.isNotBlank()) {
            try {
                val resp: TopicListResponse = localResp.fromJson()
                recommendTopicLiveData.value = resp.data
            } catch (_: Exception) {}

        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                RecommendTopicServiceHelper.loadRecommendTopics()
            }.let {
                if (localResp == it.toJson() || it.data.cardList.isEmpty()) {
                    if (it.data.cardList.isEmpty()) {
                        toast("更新失败，请检查网络")
                    }
                    return@let
                }
                MMKV.defaultMMKV().encode("recommend_topic_response", it.toJson())
                recommendTopicLiveData.value = it.data
            }
        }
    }
}