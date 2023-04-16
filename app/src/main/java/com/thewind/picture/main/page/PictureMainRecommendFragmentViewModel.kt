package com.thewind.picture.main.page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tencent.mmkv.MMKV
import com.thewind.picture.main.model.ImageRecommendTab
import com.thewind.picture.main.service.PicturePageServiceHelper
import com.thewind.util.fromJson
import com.thewind.util.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.thewind.community.image.model.ImageRecommendTabResponse
import java.lang.Exception

/**
 * @author: read
 * @date: 2023/4/15 下午9:57
 * @description:
 */
class PictureMainRecommendFragmentViewModel: ViewModel() {
    val recommendTabs: MutableLiveData<List<ImageRecommendTab>> = MutableLiveData()

    fun loadRecommendTabs(src: Int) {
        viewModelScope.launch {
            var res: ImageRecommendTabResponse? = null
            try {
                 res = MMKV.defaultMMKV().getString("picture_recommend_tabs_$src","")?.fromJson()
                if ((res?.data?.size ?: 0) > 0) {
                    recommendTabs.value = res?.data
                }
            } catch (_: Exception){}

            withContext(Dispatchers.IO) {
                PicturePageServiceHelper.loadRecommendTabs(src)
            }.let {

                if (it.toJson() != res?.toJson()) {
                    MMKV.defaultMMKV().encode("picture_recommend_tabs_$src", it.toJson())
                    recommendTabs.value = it.data
                }

            }
        }
    }
}