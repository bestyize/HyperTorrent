package com.thewind.community.comment.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.community.comment.model.Comment
import com.thewind.community.comment.service.CommentServiceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/4/1 下午10:36
 * @description:
 */
class CommentViewModel : ViewModel() {

    val commentLiveData: MutableLiveData<List<Comment>> = MutableLiveData()


    fun loadCommentList(postId: String, currPage: Int = 1) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                CommentServiceHelper.loadCommentList(postId, currPage).data
            }.let {
                commentLiveData.value = it
            }
        }
    }



}