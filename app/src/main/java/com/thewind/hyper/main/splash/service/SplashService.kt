package com.thewind.hyper.main.splash.service

import com.thewind.hyper.main.splash.model.StartCheckResponse
import com.thewind.util.RetrofitDefault
import retrofit2.Call
import retrofit2.http.POST

/**
 * @author: read
 * @date: 2023/4/12 上午12:48
 * @description:
 */
interface SplashService {

    @POST("/community/api/splash/startup")
    fun startup(): Call<StartCheckResponse>

}

object SplashServiceHelper {

    fun startupCheck(): StartCheckResponse {
        try {
            return RetrofitDefault.create(SplashService::class.java).startup().execute().body()?:StartCheckResponse()
        } catch (_:java.lang.Exception) {
            return StartCheckResponse()
        }

    }
}