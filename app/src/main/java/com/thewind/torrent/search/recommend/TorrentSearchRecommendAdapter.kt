package com.thewind.torrent.search.recommend

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.thewind.torrent.search.TorrentSearchFragment
import com.thewind.torrent.search.model.TorrentSource

/**
 * @author: read
 * @date: 2023/3/18 下午11:38
 * @description:
 */
class TorrentSearchRecommendAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val list: MutableList<TorrentSource>
) : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return TorrentSearchFragment.newInstance(list[position])
    }


}