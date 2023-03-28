package com.thewind.user.center

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.user.bean.UserInfo
import com.thewind.user.login.AccountHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/3/29 上午2:16
 * @description:
 */
class UserCenterViewModel: ViewModel() {

    val tabsListData: MutableLiveData<MutableList<String>> = MutableLiveData()

    val userInfoLiveData: MutableLiveData<UserInfo> = MutableLiveData()


    fun loadTabs() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                UserCenterService.loadTabs()
            }.let {
                tabsListData.value = it
            }
        }
    }

    fun loadUserInfo(uid: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                UserCenterService.loadUserInfo(uid)
            }.let {
                userInfoLiveData.value = it
            }
        }
    }

}


object UserCenterService {
    fun loadTabs(): MutableList<String> {
        return mutableListOf("笔记", "收藏")
    }

    fun loadUserInfo(uid: Long): UserInfo {
        return UserInfo().apply {
            this.uid = uid
            this.headerUrl = "https://th.bing.com/th/id/OIG.E5XHeQf4XBhsxhHToj3K?pid=ImgGn"
            this.userName = "亦泽"
            this.fansCount = 8888
            this.followCount = 66
            this.selfDesc = "资深开发"
        }
    }
}