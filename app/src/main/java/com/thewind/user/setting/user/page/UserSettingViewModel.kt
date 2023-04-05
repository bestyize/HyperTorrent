package com.thewind.user.setting.user.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.user.login.AccountHelper
import com.thewind.user.setting.user.model.UpdateUserInfoResponse
import com.thewind.user.setting.user.service.UpdateUserInfoServiceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserSettingViewModel : ViewModel() {

    val userNameLiveData: MutableLiveData<UpdateUserInfoResponse> = MutableLiveData()

    val passwordLiveData: MutableLiveData<UpdateUserInfoResponse> = MutableLiveData()

    val headerLiveData: MutableLiveData<UpdateUserInfoResponse> = MutableLiveData()


    fun updateUserName(userName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                UpdateUserInfoServiceHelper.updateUserName(userName)
            }.let {
                if (it.code == 0 && it.data != null) AccountHelper.saveUserInfo(it.data!!)
                userNameLiveData.value = it
            }

        }
    }

    fun updatePassword(password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                UpdateUserInfoServiceHelper.updatePassword(password)
            }.let {
                if (it.code == 0 && it.data != null) AccountHelper.saveUserInfo(it.data!!)
                passwordLiveData.value = it
            }
        }
    }
    fun updateHeader(headerFilePath: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                UpdateUserInfoServiceHelper.updateHeader(headerFilePath)
            }.let {
                headerLiveData.value = it
            }
        }
    }

}