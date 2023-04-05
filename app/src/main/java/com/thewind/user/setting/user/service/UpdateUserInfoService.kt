package com.thewind.user.setting.user.service

import com.thewind.community.editor.service.UploadResponse
import com.thewind.community.editor.service.UploadService
import com.thewind.user.setting.user.model.UpdateUserInfoResponse
import com.thewind.util.RetrofitDefault
import com.thewind.util.isPicture
import com.thewind.util.toast
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File


object UpdateUserInfoServiceHelper {
    fun updateUserName(userName: String): UpdateUserInfoResponse {
        return RetrofitDefault.create(UpdateUserInfoService::class.java).updateUserName(userName).execute().body() ?: UpdateUserInfoResponse()
    }

    fun updatePassword(password: String): UpdateUserInfoResponse {
        return RetrofitDefault.create(UpdateUserInfoService::class.java).updatePassword(password).execute().body() ?: UpdateUserInfoResponse()
    }

    fun updateHeader(filePath: String): UpdateUserInfoResponse {
        val file = File(filePath)
        if (!file.exists() || !file.isPicture()) return UpdateUserInfoResponse()
        val filePart = MultipartBody.Part.createFormData(
            "file",
            file.name,
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        )
        val resp =
            RetrofitDefault.create(UploadService::class.java).uploadFile(filePart).execute().body()
                ?: UploadResponse()
        if (resp.code != 0 || resp.list.size == 0) {
            toast(resp.msg)
            return UpdateUserInfoResponse()
        }
        val url = resp.list[0]
        return RetrofitDefault.create(UpdateUserInfoService::class.java).updateHeader(url).execute().body()?: UpdateUserInfoResponse()

    }
}
interface UpdateUserInfoService {

    @GET("/user/api/update/name")
    fun updateUserName(@Query("userName") userName: String) : Call<UpdateUserInfoResponse>
    @GET("/user/api/update/password")
    fun updatePassword(@Query("password") password: String) : Call<UpdateUserInfoResponse>

    @GET("/user/api/update/header")
    fun updateHeader(@Query("header") header: String) : Call<UpdateUserInfoResponse>

}