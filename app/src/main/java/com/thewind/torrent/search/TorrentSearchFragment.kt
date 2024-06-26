package com.thewind.torrent.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thewind.hyper.R
import com.thewind.hyper.databinding.FragmentTorrentSearchBinding
import com.thewind.torrent.search.model.TorrentInfo
import com.thewind.torrent.search.model.TorrentSource
import com.thewind.torrent.search.recommend.TorrentSearchOperator
import com.thewind.torrent.select.TorrentSelectDialogFragment
import com.thewind.util.ClipboardUtil
import com.thewind.util.toast
import com.thewind.widget.bottomsheet.CommonBottomSheetDialogFragment


class TorrentSearchFragment(
    private val torrentSource: TorrentSource,
    private var searchOperatorLiveData: MutableLiveData<TorrentSearchOperator>
) : Fragment() {
    private lateinit var binding: FragmentTorrentSearchBinding
    private lateinit var vm: TorrentSearchViewModel
    private var currPage = 1
    private var isLoading: Boolean = true

    private val torrentList: MutableList<TorrentInfo> = mutableListOf()
    private val actions = mutableListOf("复制磁力链接", "下载种子文件", "解析磁力链接文件", "取消")

    private val searchOperator = TorrentSearchOperator()

    private var lastKeyWord = "last key word"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[TorrentSearchViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTorrentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        initVmObserver()
        searchOperatorLiveData.observe(viewLifecycleOwner) {
            searchOperator.title = it.title
            searchOperator.keyword = it.keyword
            if (searchOperator.title == torrentSource.title && lastKeyWord != searchOperator.keyword) {
                lastKeyWord = searchOperator.keyword
                torrentList.clear()
                binding.srfRefresh.isRefreshing = true
                vm.search(
                    keyword = searchOperator.keyword,
                    src = torrentSource.src,
                    page = currPage
                )
            }

        }
        binding.rvSearchResult.layoutManager = LinearLayoutManager(context)
        binding.rvSearchResult.adapter = TorrentSearchAdapter(torrentList) { pos ->
            val dg = CommonBottomSheetDialogFragment.newInstance(actions) { action ->
                val info = torrentList[pos]
                when (action) {
                    0 -> {
                        toast("正在请求磁力链接")
                        vm.requestMagnetLink(torrentSource.src, info.detailUrl)
                    }
                    1 -> {
                        toast("正在为您下载种子文件")
                        vm.downloadMagnetFile(torrentSource.src, info.detailUrl, title = info.title)
                    }
                    2 -> {
                        toast("正在为您解析磁力文件")
                        vm.parseOnlineMagnetFile(torrentSource.src, info.detailUrl, title = info.title)
                    }

                }
            }
            dg.showNow(childFragmentManager, "$pos")
        }

        binding.srfRefresh.setOnRefreshListener {
            if (searchOperator.keyword.isBlank()) {
                binding.srfRefresh.isRefreshing = false
                return@setOnRefreshListener
            }
            if (!isLoading) {
                isLoading = true
                binding.srfRefresh.isRefreshing = true
                currPage = 1
                val keyword = searchOperator.keyword
                vm.search(keyword, src = torrentSource.src, page = currPage)
            }

        }
        binding.srfRefresh.setColorSchemeResources(R.color.bili_pink)

        binding.rvSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var lastPos: Int = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastPos == torrentList.size - 1 && !isLoading) {
                        isLoading = true
                        val keyword = searchOperator.keyword
                        vm.search(keyword, src = torrentSource.src, page = ++currPage)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastPos =
                    (recyclerView.layoutManager as? LinearLayoutManager)?.findLastCompletelyVisibleItemPosition()
                        ?: 0
            }
        })

    }

    private fun initVmObserver() {

        vm.results.observe(viewLifecycleOwner) {
            binding.srfRefresh.isRefreshing = false
            isLoading = false
            if (currPage == 1) torrentList.clear()
            torrentList.addAll(it.toSet().toMutableList())
            binding.rvSearchResult.adapter?.notifyDataSetChanged()
            if (searchOperator.keyword.isNotEmpty() && torrentList.isEmpty()) {
                toast("没有更多资源了")
            }

        }
        vm.magnetUrlLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                ClipboardUtil.copyClipboardText(it)
                toast("成功复制到剪切板")
            }
        }
        vm.downTorrentLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                toast("种子文件下载失败")
            } else {
                toast("种子文件下载成功")
            }
        }
        vm.parseMagnetFileLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                toast("种子文件下载失败，无法解析")
            } else {
                TorrentSelectDialogFragment.newInstance(it).showNow(childFragmentManager, it)
            }
        }
    }


    companion object {

        private const val TAG = "TorrentDSearchFragment"
        @JvmStatic
        fun newInstance(
            torrentSource: TorrentSource,
            searchOperatorLiveData: MutableLiveData<TorrentSearchOperator>
        ) = TorrentSearchFragment(torrentSource, searchOperatorLiveData)
    }
}