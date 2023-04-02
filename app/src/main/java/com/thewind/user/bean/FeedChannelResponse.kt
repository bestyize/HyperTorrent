package com.thewind.user.bean


/**
 * @author: read
 * @date: 2023/4/3 上午1:43
 * @description:
 */
class FeedChannelResponse {
    var code: Int = -1
    var msg: String = ""
    var data: List<FeedChannel> = mutableListOf()
}