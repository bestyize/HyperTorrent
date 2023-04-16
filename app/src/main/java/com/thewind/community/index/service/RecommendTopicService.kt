package com.thewind.community.index.service

import com.thewind.community.index.model.RecommendTopicItem
import com.thewind.community.topic.model.TopicCardData
import com.thewind.community.topic.model.TopicCardItem
import com.thewind.community.topic.model.TopicId
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
                this.topicId = TopicId.PICTURE_PEXELS.topic
                this.title = "Pexels"
                this.desc = "超高质量的图片免费下"
                this.leftBottomText = "8000+热度"
                this.cover = "https://s.cn.bing.net/th?id=OHR.KiteDay_ZH-CN7813901578_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&qlt=50"
            },
            TopicCardItem().apply {
                this.topicId = TopicId.PICTURE_PIXBAY.topic
                this.title = "Pixbay"
                this.desc = "来自Pixbay的百万精美图片"
                this.leftBottomText = "1000+热度"
                this.cover = "https://cdn.pixabay.com/photo/2013/04/03/21/25/flower-100263_960_720.jpg"
            },
            TopicCardItem().apply {
                this.title = "聚合磁力"
                this.desc = "免费搜索下载磁力"
                this.leftBottomText = "2w+热度"
                this.cover = "https://cdn.pixabay.com/photo/2022/09/22/08/41/forest-7471935_960_720.jpg"
                this.actionUrl = "hyper://torrent/search"
            },
            TopicCardItem().apply {
                this.title = "种子编辑"
                this.desc = "洗白你的种子"
                this.leftBottomText = "7k热度"
                this.cover = "https://th.bing.com/th/id/OIG.TevKrWWtt7RIYMuQ.SSJ?pid=ImgGn"
                this.actionUrl = "https://sdjdd.github.io/whitewash-torrent/demo/"
            },
            TopicCardItem().apply {
                this.title = "4K壁纸"
                this.desc = "电脑4K壁纸免费下"
                this.leftBottomText = "1w+热度"
                this.cover = "https://th.bing.com/th/id/OIG.D4dUFPI0FNTcK5qlrc3b?pid=ImgGn"
                this.actionUrl = "https://wallpaper.mklab.eu.org/"
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
                this.title = "Pexel"
                this.desc = "超高清图片随便下"
                this.leftBottomText = "700热度"
                this.cover = "https://img1.doubanio.com/view/photo/l/public/p1030775829.webp"
                this.actionUrl = "https://www.pexels.com/zh-cn/"
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