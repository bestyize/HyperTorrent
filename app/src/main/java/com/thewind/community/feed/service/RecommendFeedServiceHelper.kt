package com.thewind.community.feed.service

import com.thewind.community.feed.model.ChannelCardResponse
import com.thewind.util.RetrofitDefault
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Exception


interface RecommendFeedService {

    @GET("/api/community/channel/cards")
    fun loadCards(@Query("channel") channel: String, @Query("page") page: Int): Call<ChannelCardResponse>
}
object RecommendFeedServiceHelper {
    fun loadCards(channel: String, page: Int): ChannelCardResponse {
        return try {
            RetrofitDefault.create(RecommendFeedService::class.java).loadCards(channel, page).execute().body() ?: ChannelCardResponse()
        } catch (_: Exception) {
            ChannelCardResponse()
        }
    }
}
