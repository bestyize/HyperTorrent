package com.thewind.community.comment.service

import com.thewind.community.comment.model.LoadCommentListResponse
import com.thewind.community.comment.model.SendCommentResponse
import com.thewind.util.RetrofitDefault
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface CommentService {
    @GET("/api/community/comment/send")
    fun sendComment(
        @Query("postId") postId: String,
        @Query("content") content: String
    ): Call<SendCommentResponse>

    @GET("/api/community/comment/list")
    fun loadCommentList(
        @Query("postId") postId: String,
        @Query("page") page: Int = 1
    ): Call<LoadCommentListResponse>
}

object CommentServiceHelper {

    fun sendComment(postId: String, content: String): SendCommentResponse {
        return RetrofitDefault.create(CommentService::class.java).sendComment(postId, content)
            .execute().body() ?: SendCommentResponse()
    }

    fun loadCommentList(postId: String, currPage: Int): LoadCommentListResponse {
        return RetrofitDefault.create(CommentService::class.java).loadCommentList(postId, currPage)
            .execute().body() ?: LoadCommentListResponse()
    }
}
