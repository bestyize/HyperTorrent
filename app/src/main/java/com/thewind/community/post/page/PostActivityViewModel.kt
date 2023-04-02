package com.thewind.community.post.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.community.post.model.PostContent
import com.thewind.community.post.service.PostServiceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/4/1 下午8:08
 * @description:
 */
class PostActivityViewModel : ViewModel() {

    val postContentLiveData: MutableLiveData<PostContent> = MutableLiveData()


    fun loadContent(postId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                PostServiceHelper.loadPostContent(postId).data
            }.let {
                postContentLiveData.value = it
            }
        }
    }


}