package com.thewind.download.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.thewind.download.page.detail.DownloadDetailListAdapter
import com.thewind.download.page.model.DownloadDisplayItem
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.FragmentDownloadBinding

class DownloadFragment : Fragment() {

    private lateinit var binding: FragmentDownloadBinding
    private lateinit var vm: DownloadFragmentViewModel

    private var list: MutableList<DownloadDisplayItem> = mutableListOf()

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
        binding.rvItems.adapter = DownloadDetailListAdapter(list)
        vm.downloadTaskListLiveDate.observe(viewLifecycleOwner) {
            list.clear()
            list.addAll(it)
            binding.rvItems.adapter?.notifyDataSetChanged()
        }
        vm.loadDownloadRecord()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DownloadFragment()
    }
}