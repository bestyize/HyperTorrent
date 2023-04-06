package com.thewind.community.index.service

import com.thewind.community.index.model.RecommendTopicItem
import com.thewind.hypertorrent.R

/**
 * @author: read
 * @date: 2023/4/7 上午1:59
 * @description:
 */
object RecommendTopicServiceHelper {

    fun loadRecommendTopicList(): List<RecommendTopicItem> {
        return listOf(
            RecommendTopicItem().apply {
                this.title = "电影"
                this.icon = R.drawable.header
            },
            RecommendTopicItem().apply {
                this.title = "音乐"
                this.icon = R.drawable.audio
            },
            RecommendTopicItem().apply {
                this.title = "磁力"
                this.icon = R.drawable.ic_volume_open
            },
            RecommendTopicItem().apply {
                this.title = "软件"
                this.icon = R.drawable.document
            },
            RecommendTopicItem().apply {
                this.title = "福利"
                this.icon = R.drawable.document
            },
            RecommendTopicItem().apply {
                this.title = "公告"
                this.icon = R.drawable.document
            }
        )
    }


}