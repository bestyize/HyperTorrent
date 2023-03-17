package com.thewind.player.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thewind.player.detail.model.PlayItem
import com.thewind.util.isVideo
import com.thewind.util.postfix
import java.io.File


/**
 * @author : 亦泽
 * @date : 2023/3/17
 * @email : zhangrui10@bilibili.com
 */
class DetailPlayerViewModel : ViewModel() {

    val playListLiveData = MutableLiveData<MutableList<PlayItem>>()

    fun loadPlayList(path: String = "") {
        val file = File(path)
        val playItems = mutableListOf<PlayItem>()
        when {
            file.isFile && file.exists() -> playItems.add(PlayItem().apply {
                url = path
                title = file.name
            })
            file.isDirectory -> {
                file.listFiles()?.filter { it.isVideo() }?.forEach {
                    playItems.add(PlayItem().apply {
                        url = it.absolutePath
                        title = it.name
                    })
                }
            }
            path.startsWith("http") || path.startsWith("https") -> playItems.add(PlayItem().apply {
                url = path
                title = path.postfix()
            })
        }
        playListLiveData.value = playItems

    }
}