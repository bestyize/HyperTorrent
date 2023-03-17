package com.thewind.torrent.local

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xunlei.download.config.TORRENT_DIR
import java.io.File

/**
 * @author: read
 * @date: 2023/3/17 上午2:20
 * @description:
 */
class LocalFileViewModel: ViewModel() {
    val path: MutableLiveData<String> = MutableLiveData(TORRENT_DIR)
    val clickItem: MutableLiveData<Int> = MutableLiveData()
    var longClickItem: MutableLiveData<Int> = MutableLiveData()

}