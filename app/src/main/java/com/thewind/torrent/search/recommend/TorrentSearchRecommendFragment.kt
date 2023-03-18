package com.thewind.torrent.search.recommend

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.FragmentTorrentSearchRecommendBinding
import com.thewind.torrent.search.model.TorrentSource

class TorrentSearchRecommendFragment : Fragment() {

    private lateinit var binding: FragmentTorrentSearchRecommendBinding
    private lateinit var vm: TorrentSearchRecommendViewModel

    private val sourceList: MutableList<TorrentSource> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[TorrentSearchRecommendViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTorrentSearchRecommendBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vpContainer.offscreenPageLimit = 20
        binding.vpContainer.adapter =
            TorrentSearchRecommendAdapter(childFragmentManager, lifecycle, sourceList)
        context?.resources?.getColor(R.color.bili_pink)?.let { biliPink ->
            binding.searchTab.tabRippleColor = ColorStateList.valueOf(biliPink)
        }

        vm.sources.observe(viewLifecycleOwner) {
            sourceList.clear()
            sourceList.addAll(it)
            binding.vpContainer.adapter?.notifyDataSetChanged()
            TabLayoutMediator(binding.searchTab, binding.vpContainer, false) { tab, pos ->
                tab.text = sourceList[pos].title
            }.attach()
        }
        vm.loadSource()

    }

    companion object {

        @JvmStatic
        fun newInstance() = TorrentSearchRecommendFragment()
    }
}