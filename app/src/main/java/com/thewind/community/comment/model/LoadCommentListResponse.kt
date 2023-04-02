package com.thewind.community.comment.model

/**
 * @author: read
 * @date: 2023/4/3 上午1:37
 * @description:
 */
class LoadCommentListResponse {
    var code: Int = -1
    var msg: String = ""
    var data: List<Comment> = mutableListOf()
}