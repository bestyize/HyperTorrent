package com.thewind.community.comment.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.community.comment.service.CommentService
import com.thewind.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/4/2 上午1:22
 * @description:
 */
class PopCommentEditorViewModel : ViewModel() {

    val commentStatus: MutableLiveData<Boolean> = MutableLiveData()

    fun sendComment(postId: String, comment: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                CommentService.sendComment(postId, comment)
            }
            commentStatus.value = true
        }
    }
}