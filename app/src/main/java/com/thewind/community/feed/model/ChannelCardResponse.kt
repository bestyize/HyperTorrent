package com.thewind.community.feed.model

/**
 * @author: read
 * @date: 2023/4/3 上午1:24
 * @description:
 */
class ChannelCardResponse {
    var code: Int = -1
    var msg: String = ""
    var data: List<RecommendFeetCard> = mutableListOf()
}