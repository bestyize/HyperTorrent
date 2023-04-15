package com.thewind.picture.main.service

import com.thewind.picture.main.model.ImageType
import com.thewind.picture.main.model.PictureRecommendTab
import com.thewind.picture.main.model.PictureRecommendTabsResponse
import com.thewind.picture.main.model.PixbayImageQuery
import com.thewind.picture.main.model.ImageSearchResponse
import com.thewind.util.RetrofitDefault
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Exception

/**
 * @author: read
 * @date: 2023/4/15 下午8:24
 * @description:
 */

interface PicturePageService {

    @GET("/community/api/picture/search")
    fun search(@Query("params") params: String): Call<ImageSearchResponse>

    @GET("/community/api/picture/recommend/tabs")
    fun tabs(): Call<PictureRecommendTabsResponse>

}

object PicturePageServiceHelper {

    fun loadPictures(req: PixbayImageQuery): ImageSearchResponse {
        try {
            return RetrofitDefault.create(PicturePageService::class.java).search(req.toUrlQueryWitnBase64()).execute().body() ?: ImageSearchResponse()
        } catch (_:Exception) {}
        return ImageSearchResponse()
    }

    fun loadRecommendTabs(): PictureRecommendTabsResponse{
        try {
            return RetrofitDefault.create(PicturePageService::class.java).tabs().execute().body() ?: PictureRecommendTabsResponse()
        } catch (_: Exception) {}
        return PictureRecommendTabsResponse()
    }


    fun loadDefaultRecommendTabs(): PictureRecommendTabsResponse{
        val list: MutableList<PictureRecommendTab> = mutableListOf()
        list.add(PictureRecommendTab().apply {
            this.name = "推荐"
            this.query = PixbayImageQuery().apply {
                this.page = 1
                this.lang = "zh"
                this.num = 20
                this.q = "精彩"
            }
        })
        list.add(PictureRecommendTab().apply {
            this.name = "壁纸"
            this.query = PixbayImageQuery().apply {
                this.page = 1
                this.lang = "zh"
                this.num = 20
                this.q = "壁纸"
            }
        })
        list.add(PictureRecommendTab().apply {
            this.name = "美女"
            this.query = PixbayImageQuery().apply {
                this.page = 1
                this.lang = "zh"
                this.num = 20
                this.q = "美女"
            }
        })
        list.add(PictureRecommendTab().apply {
            this.name = "风景"
            this.query = PixbayImageQuery().apply {
                this.page = 1
                this.lang = "zh"
                this.num = 20
                this.q = "风景"
            }
        })
        list.add(PictureRecommendTab().apply {
            this.name = "艺术"
            this.query = PixbayImageQuery().apply {
                this.page = 1
                this.imageType = ImageType.ILLUSTRATION.type
                this.lang = "zh"
                this.num = 20
                this.q = "艺术"
            }
        })
        list.add(PictureRecommendTab().apply {
            this.name = "科技"
            this.query = PixbayImageQuery().apply {
                this.page = 1
                this.imageType = ImageType.ILLUSTRATION.type
                this.lang = "zh"
                this.num = 20
                this.q = "科技"
            }
        })
        list.add(PictureRecommendTab().apply {
            this.name = "插画"
            this.query = PixbayImageQuery().apply {
                this.page = 1
                this.lang = "zh"
                this.num = 20
                this.q = "插画"
            }
        })
        return PictureRecommendTabsResponse().apply {
            code = 0
            data = list
        }
    }

}