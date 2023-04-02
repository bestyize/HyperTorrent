package com.thewind.community.post.service

import com.thewind.community.post.model.PostContent
import com.thewind.community.post.model.PostContentResponse
import com.thewind.util.RetrofitDefault
import com.thewind.viewer.image.model.ImageDetail
import com.thewind.viewer.image.model.ImageDisplayStyle
import retrofit2.Call


interface PostContentService {
    fun loadPostContent(postId: String): Call<PostContentResponse>
}

object PostServiceHelper {
    fun loadPostContent(postId: String): PostContentResponse {
        return RetrofitDefault.create(PostContentService::class.java).loadPostContent(postId).execute().body() ?: PostContentResponse()
    }

}