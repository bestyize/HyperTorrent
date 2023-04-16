package com.thewind.picture.main.service

import android.util.Log
import com.thewind.util.RetrofitDefault
import retrofit2.Call
import retrofit2.http.GET
import com.thewind.picture.main.model.ImageRecommendTab
import retrofit2.http.Query
import xyz.thewind.community.image.model.ImageRecommendTabResponse
import xyz.thewind.community.image.model.ImageSearchResponse
import xyz.thewind.community.image.model.ImageSrc
import java.lang.Exception

/**
 * @author: read
 * @date: 2023/4/15 下午8:24
 * @description:
 */

interface PicturePageService {

    @GET("/community/api/picture/search")
    fun search(@Query("keyword") keyword: String?,@Query("page") page: Int?,@Query("num") num: Int?,@Query("src") src: Int?): Call<ImageSearchResponse>

    @GET("/community/api/picture/recommend/tabs")
    fun tabs(@Query("src") src: Int?): Call<ImageRecommendTabResponse>

}

object PicturePageServiceHelper {
    private const val TAG = "PicturePageServiceHelper"

    fun loadPictures(keyword: String?, page: Int?, num: Int?, src: Int?): ImageSearchResponse {
        try {
            return RetrofitDefault.create(PicturePageService::class.java).search(keyword?:"风景", page?:1, num?:20, src?:0).execute().body() ?: ImageSearchResponse()
        } catch (_:Exception) {
            Log.i(TAG, "loadPictures, falied")
        }
        return ImageSearchResponse()
    }

    fun loadRecommendTabs(src: Int?): ImageRecommendTabResponse{
        try {
            return RetrofitDefault.create(PicturePageService::class.java).tabs(src).execute().body() ?: ImageRecommendTabResponse()
        } catch (_: Exception) {}
        return loadDefaultRecommendTabs(src?:ImageSrc.PEXELS.src)
    }


    fun loadDefaultRecommendTabs(src: Int): ImageRecommendTabResponse {
        val resp = mutableListOf<ImageRecommendTab>()
        resp.add(ImageRecommendTab().apply {
            this.src = src
            this.title = "推荐"
            this.query = "精彩"
        })
        resp.add(ImageRecommendTab().apply {
            this.src = src
            this.title = "壁纸"
            this.query = "壁纸"
        })
        resp.add(ImageRecommendTab().apply {
            this.src = src
            this.title = "城市"
            this.query = "城市"
        })
        resp.add(ImageRecommendTab().apply {
            this.src = src
            this.title = "艺术"
            this.query = "艺术"
        })
        resp.add(ImageRecommendTab().apply {
            this.src = src
            this.title = "美女"
            this.query = "美女"
        })
        resp.add(ImageRecommendTab().apply {
            this.src = src
            this.title = "风景"
            this.query = "风景"
        })
        resp.add(ImageRecommendTab().apply {
            this.src = src
            this.title = "创意"
            this.query = "创意"
        })
        resp.add(ImageRecommendTab().apply {
            this.src = src
            this.title = "日落"
            this.query = "日落"
        })
        resp.add(ImageRecommendTab().apply {
            this.src = src
            this.title = "歌手"
            this.query = "歌手"
        })
        return ImageRecommendTabResponse().apply {
            this.code = 0
            this.data = resp
        }
    }
}