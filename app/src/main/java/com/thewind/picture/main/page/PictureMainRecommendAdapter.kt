package com.thewind.picture.main.page

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.thewind.picture.feed.page.PictureFeedFragment
import com.thewind.picture.main.model.ImageRecommendTab

/**
 * @author: read
 * @date: 2023/4/15 下午10:01
 * @description:
 */
class PictureMainRecommendAdapter(fm: FragmentManager,
                                  lifecycle: Lifecycle,
                                  val list: MutableList<ImageRecommendTab> = mutableListOf()): FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return PictureFeedFragment.newInstance(list[position])
    }
}