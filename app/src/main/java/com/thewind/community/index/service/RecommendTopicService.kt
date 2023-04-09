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
                this.title = "无名音乐"
                this.icon = R.drawable.audio
                this.url = "https://thewind.xyz"
            },
            RecommendTopicItem().apply {
                this.title = "种子洗白"
                this.icon = R.drawable.ic_volume_open
                this.url = "https://sdjdd.github.io/whitewash-torrent/demo/"
            },
            RecommendTopicItem().apply {
                this.title = "StableDiff"
                this.icon = R.drawable.document
                this.url = "https://stablediffusionweb.com/#demo"
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