package com.thewind.hypertorrent.main.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.hypertorrent.main.splash.model.StartCheckResponse
import com.thewind.hypertorrent.main.splash.service.SplashServiceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/4/11 下午11:55
 * @description:
 */
class SplashViewModel : ViewModel() {

    val startupCheckLiveData: MutableLiveData<StartCheckResponse> = MutableLiveData()

    fun checkStartUp() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                SplashServiceHelper.startupCheck()
            }.let {
                startupCheckLiveData.value = it
            }
        }
    }
}