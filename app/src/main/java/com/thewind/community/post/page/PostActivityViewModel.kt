package com.thewind.community.post.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.community.post.model.CollectResponse
import com.thewind.community.post.model.DeletePostResponse
import com.thewind.community.post.model.LikeResponse
import com.thewind.community.post.model.PostContent
import com.thewind.community.post.service.PostServiceHelper
import com.thewind.user.bean.AttentionStatus
import com.thewind.user.service.UserCenterServiceHelper
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
    val attentionLiveData: MutableLiveData<AttentionStatus> = MutableLiveData()
    var likeLiveData: MutableLiveData<LikeResponse> = MutableLiveData()
    var collectLiveData: MutableLiveData<CollectResponse> = MutableLiveData()
    var deletePostLiveData: MutableLiveData<DeletePostResponse> = MutableLiveData()


    fun loadContent(postId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                PostServiceHelper.loadPostContent(postId).data
            }.let {
                postContentLiveData.value = it
            }
        }
    }

    fun attentionUser(uid: Long, query: Boolean, follow: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                UserCenterServiceHelper.attention(uid, query, follow)
            }.let {
                attentionLiveData.value = it.data
            }
        }
    }

    fun like(postId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                PostServiceHelper.like(postId)
            }.let {
                likeLiveData.value = it
            }
        }
    }

    fun collect(postId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                PostServiceHelper.collect(postId)
            }.let {
                collectLiveData.value = it
            }
        }
    }

    fun delete(postId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                PostServiceHelper.delete(postId)
            }.let {
                deletePostLiveData.value = it
            }
        }
    }


}