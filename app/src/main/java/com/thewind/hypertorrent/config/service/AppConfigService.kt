package com.thewind.hypertorrent.config.service

import com.tencent.mmkv.MMKV
import com.thewind.hypertorrent.config.model.AppConfig
import com.thewind.hypertorrent.config.model.AppConfigResponse
import com.thewind.util.RetrofitDefault
import com.thewind.util.toJson
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.POST

/**
 * @author: read
 * @date: 2023/4/12 上午2:06
 * @description:
 */
interface AppConfigService {

    @POST("/community/api/appconfig")
    fun getConfig(): Call<AppConfigResponse>

}

object AppConfigServiceHelper {

    fun updateConfig(): AppConfig {
        return getConfig().data?:AppConfig()
    }

    private fun getConfig(): AppConfigResponse{
        return try {
            RetrofitDefault.create(AppConfigService::class.java).getConfig().execute().body() ?: AppConfigResponse()
        } catch (_: java.lang.Exception) {
            AppConfigResponse()
        }

    }
}