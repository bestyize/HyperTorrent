package com.thewind.picture.main.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.picture.main.model.ImageRecommendTab
import com.thewind.picture.main.service.PicturePageServiceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: read
 * @date: 2023/4/15 下午9:57
 * @description:
 */
class PictureMainRecommendFragmentViewModel: ViewModel() {
    val recommendTabs: MutableLiveData<List<ImageRecommendTab>> = MutableLiveData()

    fun loadRecommendTabs(src: Int) {
        viewModelScope.launch {
            recommendTabs.value = PicturePageServiceHelper.loadDefaultRecommendTabs(src).data
            withContext(Dispatchers.IO) {
                PicturePageServiceHelper.loadRecommendTabs(src)
            }.let {
                val data = recommendTabs.value?: listOf()
                var changed = false
                if (data.size == it.data.size) {
                    data.forEachIndexed { index, tab ->
                        if (tab.query != it.data[index].query){
                            changed = true
                        }
                    }
                } else {
                    changed = true
                }
                if (changed) {
                    recommendTabs.value = it.data
                }

            }
        }
    }
}