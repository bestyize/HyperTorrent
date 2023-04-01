package com.thewind.community.editor.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.viewer.image.model.ImageDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/4/2 上午2:54
 * @description:
 */
class ImagePickerViewModel: ViewModel() {
    val imageListLiveData: MutableLiveData<List<String>> = MutableLiveData()
    val publishStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()


    fun loadImageList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mutableListOf<String>()
            }.let {
                imageListLiveData.value = it
            }
        }
    }

    fun publish(title: String, content: String, arrayList: ArrayList<ImageDetail>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                true
            }.let {
                publishStatusLiveData.value = it
            }
        }
    }
}