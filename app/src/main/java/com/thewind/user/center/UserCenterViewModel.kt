package com.thewind.user.center

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.user.bean.FeedChannel
import com.thewind.user.bean.User
import com.thewind.user.bean.UserInfo
import com.thewind.user.login.AccountHelper
import com.thewind.user.service.UserCenterServiceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/3/29 上午2:16
 * @description:
 */
class UserCenterViewModel : ViewModel() {

    val tabsListData: MutableLiveData<List<FeedChannel>> = MutableLiveData()

    val userInfoLiveData: MutableLiveData<UserInfo> = MutableLiveData()

    val localUserInfo: MutableLiveData<User> = MutableLiveData()


    fun loadTabs(uid: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                UserCenterServiceHelper.loadUserTabs(uid).data
            }.let {
                tabsListData.value = it
            }
        }
    }

    fun loadUserInfo(uid: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                UserCenterServiceHelper.loadUserInfo(uid).data
            }.let {
                userInfoLiveData.value = it
            }
        }
    }

    fun loadLocalUserInfo() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                AccountHelper.loadUserInfo()
            }.let {
                localUserInfo.value = it
            }
        }
    }

}

object UserCenterService {
    fun loadTabs(): MutableList<FeedChannel> {
        return mutableListOf(FeedChannel().apply {
            title = "笔记"
            channelId = "0"
        }, FeedChannel().apply {
            title = "收藏"
            channelId = "1"
        })
    }
}