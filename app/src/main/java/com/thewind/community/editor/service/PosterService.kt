package com.thewind.community.editor.service

import com.thewind.community.post.model.PostContent
import com.thewind.viewer.image.model.ImageDetail


/**
 * @author: read
 * @date: 2023/4/2 上午3:03
 * @description:
 */
object PosterService {

    fun publish(title: String, content: String, arrayList: ArrayList<ImageDetail>): Boolean {
        val content = PostContent().apply {
            this.title = title
            this.content = content
            this.images = arrayList
        }
        return true
    }
}