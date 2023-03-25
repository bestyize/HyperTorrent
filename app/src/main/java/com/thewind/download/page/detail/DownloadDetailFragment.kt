package com.thewind.download.page.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.thewind.hypertorrent.databinding.FragmentDownloadDetailBinding
import com.thewind.util.LocalFileUtil
import com.xunlei.download.provider.TorrentTaskHelper
import com.xunlei.downloadlib.XLDownloadManager
import com.xunlei.service.database.bean.DownloadTaskBean
import java.io.File

class DownloadDetailFragment(private var stableTaskId: String) : Fragment() {
    private lateinit var binding: FragmentDownloadDetailBinding
    private lateinit var vm: DownloadDetailViewModel
    private var task: DownloadTaskBean = DownloadTaskBean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[DownloadDetailViewModel::class.java]
        activity?.title = "任务详情"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.layoutManager = LinearLayoutManager(context)
        binding.rvItems.adapter = DownloadDetailListAdapter(task.fileItemList) { pos, action ->
            if (pos >= task.fileItemList.size) return@DownloadDetailListAdapter
            val item = task.fileItemList[pos]
            when(action) {
                Action.OPEN_FILE.action -> {
                    LocalFileUtil.openFile(activity, File(item.savePath))
                }

                Action.PAUSE_DOWNLOAD.action -> {
                    TorrentTaskHelper.instance.pauseTask(task) {}
                }

                Action.START_DOWNLOAD.action -> {
                    TorrentTaskHelper.instance.startTask(item.tempTaskId)
                }
            }
        }

        vm.downloadTaskLiveData.observe(viewLifecycleOwner) {
            task.fileItemList.clear()
            task.fileItemList.addAll(it.fileItemList)
            binding.rvItems.adapter?.notifyDataSetChanged()
        }

        vm.loadDetailList(stableTaskId)
    }

    companion object {
        @JvmStatic
        fun newInstance(stableTaskId: String) = DownloadDetailFragment(stableTaskId)
    }

}