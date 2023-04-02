package com.thewind.user.service

import com.thewind.user.bean.*
import com.thewind.util.RetrofitDefault
import retrofit2.Call
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author: read
 * @date: 2023/4/3 上午1:06
 * @description:
 */
interface UserCenterService {

    @GET("/api/community/user/userinfo")
    fun loadUserInfo(@Query("uid") uid: Long): Call<UserInfoResponse>

    @GET("/api/community/user/tabs")
    fun loadUserTabs(@Query("uid") uid: Long): Call<FeedChannelResponse>

    @GET("/api/community/user/attention")
    fun attention(@Query("uid") uid: Long, @Query("query") query: Boolean, @Query("follow") follow: Boolean): Call<AttentionResponse>

}

object UserCenterServiceHelper {
    fun loadUserInfo(uid: Long): UserInfoResponse {
        return RetrofitDefault.create(UserCenterService::class.java).loadUserInfo(uid).execute().body() ?: UserInfoResponse()
    }

    fun loadUserTabs(uid: Long): FeedChannelResponse {
        return RetrofitDefault.create(UserCenterService::class.java).loadUserTabs(uid).execute().body() ?: FeedChannelResponse()
    }

    fun attention(uid: Long, query: Boolean = true, follow: Boolean): AttentionResponse{
        return RetrofitDefault.create(UserCenterService::class.java).attention(uid, query, follow).execute().body() ?: AttentionResponse()
    }
}