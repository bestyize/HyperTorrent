package com.thewind.download.page

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.thewind.download.page.detail.DownloadDetailListAdapter
import com.thewind.download.page.model.DownloadDisplayItem
import com.thewind.hyper.R
import com.thewind.hyper.databinding.FragmentDownloadBinding
import com.thewind.widget.bottomsheet.CommonBottomSheetDialogFragment
import com.xunlei.download.provider.TorrentTaskHelper
import com.xunlei.service.database.TorrentDBHelper
import com.xunlei.service.database.bean.DownloadTaskBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DownloadFragment : Fragment() {

    private lateinit var binding: FragmentDownloadBinding
    private lateinit var vm: DownloadFragmentViewModel

    private var list: MutableList<DownloadTaskBean> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[DownloadFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.layoutManager = LinearLayoutManager(context)
        binding.rvItems.adapter = DownloadListAdapter(list) {
            if (it >= list.size) return@DownloadListAdapter
            val item = list[it]
            CommonBottomSheetDialogFragment.newInstance(mutableListOf( "查看详情", "继续下载", "暂停下载", "删除", "取消")){ pos ->
                when (pos) {
                    0 -> {
                        val intent = Intent(activity, DownloadActivity::class.java)
                        intent.putExtra("stable_task_id", item.stableTaskId)
                        startActivity(intent)
                    }
                    1 -> {
                        TorrentTaskHelper.instance.startTask(item.tempTaskId)
                    }

                    2 -> {
                        TorrentTaskHelper.instance.pauseTask(item) {
                            binding.rvItems.adapter?.notifyItemChanged(it)
                        }
                    }
                    3 -> {
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                TorrentDBHelper.removeDownloadTaskRecord(item.stableTaskId)
                            }
                            list.remove(item)
                            binding.rvItems.adapter?.notifyItemRemoved(it)
                        }

                    }
                }
            }.showNow(childFragmentManager, "")
        }
        vm.downloadTaskListLiveDate.observe(viewLifecycleOwner) {
            list.clear()
            list.addAll(it)
            binding.srfRefresh.isRefreshing = false
            binding.rvItems.adapter?.notifyDataSetChanged()
        }
        binding.srfRefresh.setColorSchemeColors(resources.getColor(R.color.bili_pink))
        binding.srfRefresh.setOnRefreshListener {
            binding.srfRefresh.isRefreshing = true
            vm.loadDownloadRecord()

        }

        vm.loadDownloadRecord()

        lifecycleScope.launch {
            while (true) {
                vm.loadDownloadRecord()
                delay(1000)
            }
        }
    }

    private fun actionDownload() {

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DownloadFragment()
    }
}