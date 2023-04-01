package com.thewind.community.comment.service

import com.thewind.community.comment.model.Comment

object CommentService {

    fun sendComment(postId: String, content: String) {

    }

    fun loadCommentList(postId: String, currPage: Int): MutableList<Comment> {
        return mutableListOf(
            Comment().apply {
                this.id = "12345"
                this.content = "春江潮水连海平，海上明月共潮生，滟滟随波千万里，何处春江不月明"
                this.likeCount = 55
                this.time = System.currentTimeMillis()
                this.upHeaderUrl = "https://img0.baidu.com/it/u=949547577,2571626573&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=800"
                this.upId = 10000
                this.upName = "亦泽"
                this.postId = postId
            },
            Comment().apply {
                this.id = "12333"
                this.content = "不知道怎么搞的"
                this.likeCount = 55
                this.time = System.currentTimeMillis()
                this.upHeaderUrl = "https://img2.baidu.com/it/u=1250551608,2180019998&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"
                this.upId = 10001
                this.upName = "往往"
                this.postId = postId
            },
            Comment().apply {
                this.id = "12333"
                this.content = "随风而是2233"
                this.likeCount = 55
                this.time = System.currentTimeMillis()
                this.upHeaderUrl = "https://img2.baidu.com/it/u=1250551608,2180019998&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"
                this.upId = 10001
                this.upName = "往往"
                this.postId = postId
            },
            Comment().apply {
                this.id = "12333"
                this.content = "不知道怎么搞的"
                this.likeCount = 55
                this.time = System.currentTimeMillis()
                this.upHeaderUrl = "https://img2.baidu.com/it/u=1250551608,2180019998&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"
                this.upId = 10001
                this.upName = "往往"
                this.postId = postId
            },
            Comment().apply {
                this.id = "12333"
                this.content = "不知道怎么搞的"
                this.likeCount = 55
                this.time = System.currentTimeMillis()
                this.upHeaderUrl = "https://img2.baidu.com/it/u=1250551608,2180019998&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"
                this.upId = 10001
                this.upName = "往往"
                this.postId = postId
            },
            Comment().apply {
                this.id = "12333"
                this.content = "不知道怎么搞的"
                this.likeCount = 55
                this.time = System.currentTimeMillis()
                this.upHeaderUrl = "https://img2.baidu.com/it/u=1250551608,2180019998&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"
                this.upId = 10001
                this.upName = "往往"
                this.postId = postId
            }
        )
    }
}
