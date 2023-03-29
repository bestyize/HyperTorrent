package com.thewind.community.feed.service

import com.thewind.community.feed.model.RecommendFeetCard

object RecommendFeedService {
    fun loadCards(channel: String, page: Int): MutableList<RecommendFeetCard> {
        return mockCard()
    }

    private fun mockCard(): MutableList<RecommendFeetCard> {
        return mutableListOf(
            RecommendFeetCard().apply {
                title = "使用ChatGPT生成的第一个程序"
                cover = "https://images.pexels.com/photos/1670187/pexels-photo-1670187.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
                rightIcon = "https://cdn4.iconfinder.com/data/icons/basic-ui-2-line/32/heart-love-like-likes-loved-favorite-64.png"
                rightText = "9999+"
                upName = "亦泽"
                upIcon = "https://p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-avt-0015_dc36609034f68f6629bb59bdfb4d10f3.jpeg"
            },
            RecommendFeetCard().apply {
                title = "好玩又轻松的AI图片生成框架，来试一下吧"
                cover = "https://images.pexels.com/photos/2647990/pexels-photo-2647990.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
                rightIcon = "https://cdn4.iconfinder.com/data/icons/basic-ui-2-line/32/heart-love-like-likes-loved-favorite-64.png"
                rightText = "8787"
                upName = "就成"
                upIcon = "https://p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-avt-0015_dc36609034f68f6629bb59bdfb4d10f3.jpeg"
            },
            RecommendFeetCard().apply {
                title = "大海、浪花、和你"
                cover = "https://images.pexels.com/photos/937783/pexels-photo-937783.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
                rightIcon = "https://cdn4.iconfinder.com/data/icons/basic-ui-2-line/32/heart-love-like-likes-loved-favorite-64.png"
                rightText = "10w+"
                upName = "混合"
                upIcon = "https://p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-avt-0015_dc36609034f68f6629bb59bdfb4d10f3.jpeg"
            },
            RecommendFeetCard().apply {
                title = "敏感的色调让人印象深刻"
                cover = "https://images.pexels.com/photos/1078983/pexels-photo-1078983.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
                rightIcon = "https://cdn4.iconfinder.com/data/icons/basic-ui-2-line/32/heart-love-like-likes-loved-favorite-64.png"
                rightText = "1k+"
                upName = "请求"
                upIcon = "https://p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-avt-0015_dc36609034f68f6629bb59bdfb4d10f3.jpeg"
            },
            RecommendFeetCard().apply {
                title = "秋天的云彩都去哪里了"
                cover = "https://images.pexels.com/photos/925743/pexels-photo-925743.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
                rightIcon = "https://cdn4.iconfinder.com/data/icons/basic-ui-2-line/32/heart-love-like-likes-loved-favorite-64.png"
                rightText = "232"
                upName = "春风"
                upIcon = "https://p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-avt-0015_dc36609034f68f6629bb59bdfb4d10f3.jpeg"
            },
            RecommendFeetCard().apply {
                title = "春江潮水连海平"
                cover = "https://images.pexels.com/photos/359971/pexels-photo-359971.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
                rightIcon = "https://cdn4.iconfinder.com/data/icons/basic-ui-2-line/32/heart-love-like-likes-loved-favorite-64.png"
                rightText = "2233"
                upName = "春风"
                upIcon = "https://p3-pc.douyinpic.com/aweme/100x100/aweme-avatar/tos-cn-avt-0015_dc36609034f68f6629bb59bdfb4d10f3.jpeg"
            }
        )
    }
}
