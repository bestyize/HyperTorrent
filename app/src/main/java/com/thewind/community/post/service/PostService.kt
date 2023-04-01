package com.thewind.community.post.service

import com.thewind.community.post.model.PostContent
import com.thewind.viewer.image.model.ImageDetail
import com.thewind.viewer.image.model.ImageDisplayStyle

object PostService {

    fun loadPostContent(postId: String): PostContent {

        val arrayList = arrayListOf<ImageDetail>().apply {
            add(ImageDetail().apply {
                url = "https://cdn.pixabay.com/photo/2023/03/25/16/02/hummingbird-7876355_960_720.jpg"
                desc = "蜂鸟"
                style = ImageDisplayStyle.CENTER_CROP.style
            })
            add(ImageDetail().apply {
                url = "https://cdn.pixabay.com/photo/2015/07/05/13/44/beach-832346_960_720.jpg"
                desc = "海滩"
                style = ImageDisplayStyle.CENTER_CROP.style
            })
        }
        return PostContent().apply {
            this.id = postId
            this.title = "Stable Diffusion Promo Recommend"
            this.content = "据《证券时报》报道，3月31日夜间，华为终端BG CEO、智能汽车解决方案BU CEO余承东亲自下达指令：问界门店将于4月1日开始拆除所有相关华为字样的宣传物料。观察者网就此向华为相关人士求证，对方未予确认。但今日早间观察者网探访华为旗舰店，发现问界展车上的“HUAWEI”字样确已消失。\n" +
                    "\n" +
                    "观察者网获悉，3月31日早些时间，https://www.baidu.com 华为创始人任正非亲自签发对于汽车业务的决策公告，再次强调华为不造车，有效期5年，并且对华为标志在汽车设计上的露出提出严格要求，强调不能使用华为/HUAWEI出现在整车宣传和外观上。\n" +
                    "\n" +
                    "4月1日，观察者网在上海市长宁来福士的华为门店看到，现场展出的问界样车上，不再有“华为”字样，留白处存在贴纸刚被撕掉的痕迹。而早些时候，多地门店曾在样车车牌位置展示“”HUAWEI问界“字样。"
            this.likeCount = 999
            this.collectCount = 8787
            this.commentCount = 10
            this.upId = 100001L
            this.images = arrayList
            this.upHeaderUrl = "https://himg.bdimg.com/sys/portrait/item/public.1.7d5813a6.Jkz5AGQiqliU_mvzs9JP5w.jpg"
            this.upName = "十月初一"
        }
    }

}