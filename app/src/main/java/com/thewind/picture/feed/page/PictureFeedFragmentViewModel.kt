package com.thewind.picture.feed.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.downloader.HttpDownloader
import com.thewind.picture.main.model.PixbayImageQuery
import com.thewind.picture.main.model.ImageSearchResponse
import com.thewind.picture.main.service.PicturePageServiceHelper
import com.thewind.util.Md5Util
import com.thewind.util.postfix
import com.xunlei.download.config.BASE_PICTURE_DOWNLOAD_DIR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * @author: read
 * @date: 2023/4/15 下午9:33
 * @description:
 */
class PictureFeedFragmentViewModel : ViewModel() {

    val list: MutableLiveData<ImageSearchResponse> = MutableLiveData()
    val downloadState: MutableLiveData<Boolean> = MutableLiveData()
    val shareState: MutableLiveData<String> = MutableLiveData()

    fun loadPictureList(request: PixbayImageQuery) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                PicturePageServiceHelper.loadPictures(request)
            }.let {
                list.value = it
            }
        }
    }

    fun downloadPicture(imageUrl: String?, quity: Boolean = false) {
        imageUrl ?: return
        viewModelScope.launch {
            val filePath = BASE_PICTURE_DOWNLOAD_DIR + Md5Util.convertToMd5(imageUrl) + "." + imageUrl.postfix()
            withContext(Dispatchers.IO) {
                val file = File(filePath)
                if (file.exists() && file.length() > 0) {
                    true
                } else {
                    HttpDownloader.download(imageUrl, filePath)
                }

            }.let {
                if (quity) {
                    downloadState.value = it
                } else {
                    if (File(filePath).exists()) {
                        shareState.value = filePath
                    } else {
                        shareState.value = ""
                    }
                }


            }
        }
    }

}