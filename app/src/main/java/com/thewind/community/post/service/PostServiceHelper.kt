package com.thewind.community.post.service

import com.thewind.community.post.model.CollectResponse
import com.thewind.community.post.model.DeletePostResponse
import com.thewind.community.post.model.LikeResponse
import com.thewind.community.post.model.PostContentResponse
import com.thewind.util.RetrofitDefault
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Exception


interface PostContentService {
    @GET("/api/community/post/content")
    fun loadPostContent(@Query("postId") postId: String): Call<PostContentResponse>

    @GET("/api/community/post/like")
    fun likePost(@Query("postId") postId: String): Call<LikeResponse>

    @GET("/api/community/post/collect")
    fun collectPost(@Query("postId") postId: String): Call<CollectResponse>

    @GET("/api/community/post/delete")
    fun deletePost(@Query("postId") postId: String): Call<DeletePostResponse>

}

object PostServiceHelper {
    fun loadPostContent(postId: String): PostContentResponse {
        return try {
            RetrofitDefault.create(PostContentService::class.java).loadPostContent(postId).execute().body() ?: PostContentResponse()
        } catch (_: Exception){PostContentResponse()}
    }

    fun like(postId: String): LikeResponse {
        return try {
            RetrofitDefault.create(PostContentService::class.java).likePost(postId).execute().body() ?: LikeResponse()
        } catch (_: Exception){LikeResponse()}
    }

    fun collect(postId: String): CollectResponse {
        return try {
            RetrofitDefault.create(PostContentService::class.java).collectPost(postId).execute().body() ?: CollectResponse()
        } catch (_: Exception) {
            CollectResponse()
        }
    }

    fun delete(postId: String): DeletePostResponse {
        return try {
            RetrofitDefault.create(PostContentService::class.java).deletePost(postId).execute().body() ?: DeletePostResponse()
        } catch (_: Exception) {DeletePostResponse()}
    }

}