package com.thewind.user.service

import com.thewind.user.bean.FeedChannel
import com.thewind.user.bean.FeedChannelResponse
import com.thewind.user.bean.UserInfo
import com.thewind.user.bean.UserInfoResponse
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

}

object UserCenterServiceHelper {
    fun loadUserInfo(uid: Long): UserInfoResponse {
        return RetrofitDefault.create(UserCenterService::class.java).loadUserInfo(uid).execute().body() ?: UserInfoResponse()
    }

    fun loadUserTabs(uid: Long): FeedChannelResponse {
        return RetrofitDefault.create(UserCenterService::class.java).loadUserTabs(uid).execute().body() ?: FeedChannelResponse()
    }
}