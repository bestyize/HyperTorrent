package com.thewind.torrent.search.recommend

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
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
import com.thewind.torrent.search.TorrentSearchFragment
import com.thewind.torrent.search.model.TorrentSource
import com.thewind.util.toast

class TorrentSearchRecommendFragment : Fragment() {

    private lateinit var binding: FragmentTorrentSearchRecommendBinding
    private lateinit var vm: TorrentSearchRecommendViewModel

    private val sourceList: MutableList<TorrentSource> = mutableListOf()

    private var currTabTitle = ""

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
        Log.i(TAG, "onViewCreated, start")
        binding.vpContainer.offscreenPageLimit = 20
        binding.vpContainer.adapter =
            TorrentSearchRecommendAdapter(childFragmentManager, lifecycle, sourceList, vm.searchOperatorLiveData)
        context?.resources?.getColor(R.color.bili_pink_transport)?.let { biliPink ->
            binding.searchTab.tabRippleColor = ColorStateList.valueOf(biliPink)
        }
        binding.searchTab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currTabTitle = tab?.text?.toString() ?: ""
                vm.notifySearch(title = currTabTitle, keyword = binding.topSearchBar.etInput.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        binding.topSearchBar.tvSearch.setOnClickListener {
            toast("正在努力搜索")
            val keyword = binding.topSearchBar.etInput.text.toString()
            vm.notifySearch(title = currTabTitle, keyword = keyword)
        }


        vm.sources.observe(viewLifecycleOwner) {
            Log.i(TAG, "onViewCreated, source loaded")
            sourceList.clear()
            sourceList.addAll(it)
            if (currTabTitle.isEmpty() && sourceList.size > 0) {
                currTabTitle = sourceList[0].title
            }
            binding.vpContainer.adapter?.notifyDataSetChanged()
            TabLayoutMediator(binding.searchTab, binding.vpContainer, false) { tab, pos ->
                tab.text = sourceList[pos].title
            }.attach()
        }
        Log.i(TAG, "onViewCreated, start load tabs")
        vm.loadSource()

    }

    companion object {

        private const val TAG = "TorrentSearchRecommendFragment"

        @JvmStatic
        fun newInstance() = TorrentSearchRecommendFragment()
    }
}