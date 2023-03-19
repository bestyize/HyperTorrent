package com.thewind.viewer.code

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.io.FileUtils
import java.io.File

/**
 * @author: read
 * @date: 2023/3/20 上午4:17
 * @description:
 */
class CodeViewerViewModel :ViewModel() {

    val codeLiveData: MutableLiveData<String> = MutableLiveData()

    fun loadCode(path: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                FileUtils.readFileToString(File(path), "utf-8")
            }.let {
                codeLiveData.value = it
            }
        }
    }

}