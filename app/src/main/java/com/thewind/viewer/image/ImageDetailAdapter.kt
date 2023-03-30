package com.thewind.viewer.image

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.thewind.viewer.image.model.ImageDetail

/**
 * @author: read
 * @date: 2023/3/31 上午1:04
 * @description:
 */
class ImageDetailAdapter(fragmentManager: FragmentManager,
                         lifecycle: Lifecycle,
                         private val list: MutableList<ImageDetail>): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        val item = list[position].apply {
            title = "${position + 1}/${list.size}"
        }
        return ImageDetailFragment.newInstance(item)
    }
}