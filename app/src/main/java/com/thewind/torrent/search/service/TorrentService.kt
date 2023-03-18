package com.thewind.torrent.search.service

import com.thewind.torrent.search.model.TorrentConfigResponse
import com.thewind.torrent.search.model.TorrentSearchResponse
import com.thewind.torrent.search.model.TorrentUrlResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author: read
 * @date: 2023/3/18 下午6:46
 * @description:
 */
interface TorrentService {

    @GET("/torrent/api/search")
    fun search(
        @Query("keyword") keyword: String,
        @Query("src") src: Int,
        @Query("page") page: Int
    ): Call<TorrentSearchResponse>

    @GET("/torrent/api/magnetlink")
    fun requestMagnetLink(@Query("src") src: Int, @Query("link") link: String): Call<TorrentUrlResponse>

    @GET("/torrent/api/supportsource")
    fun requestTorrentConfig(): Call<TorrentConfigResponse>
}