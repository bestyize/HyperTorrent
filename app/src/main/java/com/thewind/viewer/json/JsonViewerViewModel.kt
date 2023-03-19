package com.thewind.viewer.json


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.util.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.io.FileUtils
import java.io.File

/**
 * @author: read
 * @date: 2023/3/20 上午2:32
 * @description:
 */
class JsonViewerViewModel : ViewModel() {
    val jsonTextLiveData: MutableLiveData<String> = MutableLiveData()

    fun loadJsonStr(path: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when {
                    path.startsWith("http") -> get(path)
                    File(path).let {
                        it.isFile && it.exists()
                    } -> {
                        FileUtils.readFileToString(File(path), "utf-8")
                    } else -> path
                }
            }.let {
                jsonTextLiveData.value = it
            }
        }
    }
}