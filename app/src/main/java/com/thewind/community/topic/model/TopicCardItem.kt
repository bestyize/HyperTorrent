package com.thewind.community.topic.model

import androidx.annotation.Keep

/**
 * @author: read
 * @date: 2023/4/13 上午2:19
 * @description:
 */
@Keep
class TopicCardItem {
    var topicId: Int = 0
    var title: String? = null
    var desc: String? = null
    var leftBottomText: String? = null
    var leftBottomIcon: String? = null
    var cover: String? = null
    var topicType: Int = 0
    var actionUrl: String? = null
}


enum class TopicId(val topic: Int) {
    WEB(0),
    PIXBAY(1),
    PEXELS(2),
    MAGNET(3),
    KONA(4)
}