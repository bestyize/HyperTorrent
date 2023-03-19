package com.thewind.download.page.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.thewind.download.page.model.DownloadDisplayItem
import com.thewind.hypertorrent.databinding.FragmentDownloadDetailBinding

class DownloadDetailFragment(private var taskId: String) : Fragment() {
    private lateinit var binding: FragmentDownloadDetailBinding
    private lateinit var vm: DownloadDetailViewModel
    private var downloadFileList: MutableList<DownloadDisplayItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[DownloadDetailViewModel::class.java]
        activity?.title = "下载详情"
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
        binding.rvItems.adapter = DownloadDetailListAdapter(downloadFileList)

        vm.detailList.observe(viewLifecycleOwner) {
            downloadFileList.clear()
            downloadFileList.addAll(it)
            binding.rvItems.adapter?.notifyDataSetChanged()
        }

        vm.loadDetailList(taskId)
    }

    companion object {
        @JvmStatic
        fun newInstance() = DownloadDetailFragment("1234")
    }

}