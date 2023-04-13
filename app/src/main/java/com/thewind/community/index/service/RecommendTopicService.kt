package com.thewind.community.index.service

import com.thewind.community.index.model.RecommendTopicItem
import com.thewind.community.topic.model.TopicCardData
import com.thewind.community.topic.model.TopicCardItem
import com.thewind.community.topic.model.TopicListResponse
import com.thewind.hyper.R

/**
 * @author: read
 * @date: 2023/4/7 上午1:59
 * @description:
 */
object RecommendTopicServiceHelper {

    fun loadRecommendTopicList(): TopicListResponse {
        val list = listOf(
            TopicCardItem().apply {
                this.title = "无名音乐"
                this.desc = "无损音乐在线下"
                this.leftBottomText = "9999+热度"
                this.cover = "https://cdn.pixabay.com/photo/2022/03/31/08/47/moon-7102510_960_720.jpg"
                this.actionUrl = "https://thewind.xyz"
            },
            TopicCardItem().apply {
                this.title = "PixBay"
                this.desc = "海量精美图片"
                this.leftBottomText = "1000+热度"
                this.cover = "https://cdn.pixabay.com/photo/2013/04/03/21/25/flower-100263_960_720.jpg"
                this.actionUrl = "https://pixabay.com/zh/photos/"
            },
            TopicCardItem().apply {
                this.title = "聚合磁力"
                this.desc = "免费搜索下载磁力"
                this.leftBottomText = "2w+热度"
                this.cover = "https://cdn.pixabay.com/photo/2022/09/22/08/41/forest-7471935_960_720.jpg"
                this.actionUrl = "hyper://torrent/search"
            },
            TopicCardItem().apply {
                this.title = "ChatGPT"
                this.desc = "强大的人工智能"
                this.leftBottomText = "7k热度"
                this.cover = "https://th.bing.com/th/id/OIG.TevKrWWtt7RIYMuQ.SSJ?pid=ImgGn"
                this.actionUrl = "https://chat.openai.com/chat"
            },
            TopicCardItem().apply {
                this.title = "必应绘图"
                this.desc = "借助DALL-E生成精美图片"
                this.leftBottomText = "10w+热度"
                this.cover = "https://th.bing.com/th/id/OIG.D4dUFPI0FNTcK5qlrc3b?pid=ImgGn"
                this.actionUrl = "https://www.bing.com/images/create"
            },
            TopicCardItem().apply {
                this.title = "TED演讲"
                this.desc = "震撼人心的演讲"
                this.leftBottomText = "8779热度"
                this.cover = "https://cdn.pixabay.com/photo/2020/06/14/10/58/london-5297395_960_720.jpg"
                this.actionUrl = "https://www.ted.com/talks"
            },
            TopicCardItem().apply {
                this.title = "吾爱破解"
                this.desc = "大佬云集的软件站"
                this.leftBottomText = "7000热度"
                this.cover = "https://down.52pojie.cn/Logo/Wallpaper/52pojie_1366x768.jpg"
                this.actionUrl = "https://52pojie.cn"
            },
            TopicCardItem().apply {
                this.title = "VideVo"
                this.desc = "免费视频素材"
                this.leftBottomText = "700热度"
                this.cover = "https://img1.doubanio.com/view/photo/l/public/p1030775829.webp"
                this.actionUrl = "https://www.videvo.net/"
            }
        )

        val data = TopicCardData().apply {
            this.aspectRadio = 1.6f
            this.cardList.addAll(list)
            this.columnCount = 2

        }

        return TopicListResponse().apply {
            this.code = 0
            this.data = data

        }
    }


}