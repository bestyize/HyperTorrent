package com.thewind.user.center

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.thewind.community.feed.page.RecommendFeedFragment
import com.thewind.user.bean.FeedChannel

/**
 * @author: read
 * @date: 2023/3/30 上午2:42
 * @description:
 */
class UserFeedAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val channels: MutableList<FeedChannel>
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return channels.size
    }

    override fun createFragment(position: Int): Fragment {
        val column = if (position % 2 == 0) 1 else 2
        return RecommendFeedFragment.newInstance(channels[position].channelId ?: "0", column)
    }
}