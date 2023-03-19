package com.thewind.viewer.json

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.FragmentJsonViewerBinding


class JsonViewerFragment private constructor(private val path: String): Fragment() {

    private lateinit var binding: FragmentJsonViewerBinding
    private lateinit var vm: JsonViewerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[JsonViewerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJsonViewerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "预览Json"
        binding.srfRefresh.setOnRefreshListener {
            binding.srfRefresh.isRefreshing = true
            vm.loadJsonStr(path)
        }
        binding.srfRefresh.setColorSchemeColors(resources.getColor(R.color.bili_pink))
        vm.jsonTextLiveData.observe(viewLifecycleOwner) {
            binding.srfRefresh.isRefreshing = false
            binding.jsonViewer.bindJson(it)
            binding.jsonViewer.expandAll()
        }
        vm.loadJsonStr(path)
        binding.srfRefresh.isRefreshing = true
    }

    companion object {
        @JvmStatic
        fun newInstance(path: String) = JsonViewerFragment(path)
    }
}