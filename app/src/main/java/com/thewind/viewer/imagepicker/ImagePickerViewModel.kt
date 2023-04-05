package com.thewind.viewer.imagepicker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.community.editor.model.ImagePickerItem
import com.thewind.community.editor.service.PosterService
import com.thewind.community.editor.tool.LocalPhotoReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/4/2 上午2:54
 * @description:
 */
class ImagePickerViewModel: ViewModel() {
    val imageListLiveData: MutableLiveData<List<ImagePickerItem>> = MutableLiveData()
    val publishStatusLiveData: MutableLiveData<String> = MutableLiveData()


    fun loadImageList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                LocalPhotoReader.loadLocalPhotoList()
            }.let {
                imageListLiveData.value = it
            }
        }
    }

    fun publish(title: String, content: String, images: List<String>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val list = PosterService.uploadImages(images)
                if (list.isNotEmpty()) {
                    PosterService.publish(title, content, list)
                } else {
                    ""
                }
            }.let {
                publishStatusLiveData.value = it
            }
        }
    }

}