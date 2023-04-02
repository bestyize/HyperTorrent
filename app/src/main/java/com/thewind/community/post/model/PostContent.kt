package com.thewind.community.post.model

import com.thewind.viewer.image.model.ImageDetail

/**
 * @author: read
 * @date: 2023/4/1 下午8:10
 * @description:
 */
class PostContent {
    var id: String? = null
    var title: String? = null
    var content: String? = null
    var images: ArrayList<ImageDetail>? = null
    var likeCount: Int = 0
    var collectCount: Int = 0
    var commentCount: Int = 0
    var postDate: Long = 0


    var userName: String? = null
    var userHeaderUrl: String? = null
    var uid: Long = -1
    var follow: Boolean = false
    var like: Boolean = false
    var collect: Boolean = false
}

