package com.thewind.community.topic.model

import androidx.annotation.Keep

/**
 * @author: read
 * @date: 2023/4/13 上午3:17
 * @description:
 */
@Keep
class TopicListResponse {
    var code = -1
    var msg: String? = null
    var data: TopicCardData = TopicCardData()
}