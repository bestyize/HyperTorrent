package com.thewind.community.comment.model

import androidx.annotation.Keep

/**
 * @author: read
 * @date: 2023/4/3 上午1:34
 * @description:
 */
@Keep
class SendCommentResponse {
    var code: Int = -1
    var msg: String = ""
    var data: Comment = Comment()
}