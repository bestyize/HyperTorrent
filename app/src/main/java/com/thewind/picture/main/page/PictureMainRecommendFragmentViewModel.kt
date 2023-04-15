package com.thewind.picture.main.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.picture.main.model.PictureRecommendTab
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
    val recommendTabs: MutableLiveData<List<PictureRecommendTab>> = MutableLiveData()

    fun loadRecommendTabs() {
        viewModelScope.launch {
            recommendTabs.value = PicturePageServiceHelper.loadDefaultRecommendTabs().data
            withContext(Dispatchers.IO) {
                PicturePageServiceHelper.loadRecommendTabs()
            }.let {
                val data = recommendTabs.value?: listOf()
                var changed = false
                if (data.size == it.data.size) {
                    data.forEachIndexed { index, tab ->
                        if (tab.query.q != it.data[index].query.q){
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