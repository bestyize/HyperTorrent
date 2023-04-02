package com.thewind.community.comment.model

import androidx.annotation.Keep

/**
 * @author: read
 * @date: 2023/4/1 下午10:38
 * @description:
 */
@Keep
class Comment {
    var id: String? = null
    var content: String? = null
    var upId: Long = -1
    var upName: String? = null
    var upHeaderUrl: String? = null
    var time: Long = 0L
    var postId: String? = null
    var likeCount: Int = 0
    var parentCommentId: String?= null
    var subCommentList = mutableListOf<Comment>()

}