package com.thewind.community.editor.service

import com.thewind.community.post.model.PostContent
import com.thewind.util.*
import com.thewind.viewer.image.model.ImageDetail
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File


/**
 * @author: read
 * @date: 2023/4/2 上午3:03
 * @description:
 */
object PosterService {

    fun publish(title: String, content: String, arrayList: ArrayList<ImageDetail>): String {
        return post("$BASE_URL/api/community/poster/publish", PostContent().apply {
            this.title = title
            this.content = content
            this.images = arrayList
        }.toJson())
    }

    fun uploadImages(list: List<String>): ArrayList<ImageDetail> {
        val fileParts = list.map { File(it) }.filter { it.exists() && it.isPicture() }.map {
            MultipartBody.Part.createFormData(
                "files",
                it.name,
                RequestBody.create(MediaType.parse("multipart/form-data"), it)
            )
        }

        val resp =
            RetrofitDefault.create(UploadService::class.java).uploadFile(fileParts).execute().body()
                ?: UploadResponse()

        if (resp.code != 0) {
            return arrayListOf()
        }
        val arrList = arrayListOf<ImageDetail>()
        resp.list.forEach {
            arrList.add(ImageDetail().apply {
                url = it
            })
        }
        return arrList
    }

}

interface UploadService {

    @Multipart
    @POST("/api/community/picture/upload")
    fun uploadFile(@Part files: List<MultipartBody.Part>): Call<UploadResponse>

    @Multipart
    @POST("/api/community/picture/upload")
    fun uploadFile(@Part files: MultipartBody.Part): Call<UploadResponse>
}

class UploadResponse {
    var code: Int = -1
    var msg: String = ""
    var list: MutableList<String> = mutableListOf()
}