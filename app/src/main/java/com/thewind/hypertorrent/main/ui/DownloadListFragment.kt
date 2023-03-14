package com.thewind.hypertorrent.main.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.FragmentDownloadListBinding
import com.thewind.hypertorrent.main.card.downloadlist.DownloadListAdapter
import com.xunlei.download.provider.TorrentTaskManager


/**
 * A simple [Fragment] subclass.
 * Use the [DownloadListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DownloadListFragment : Fragment() {

    private lateinit var binding: FragmentDownloadListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvDownloadTaskList.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.rvDownloadTaskList.adapter = DownloadListAdapter(TorrentTaskManager.instance.getTorrentTaskList())
    }

    companion object {
        @JvmStatic
        fun newInstance() = DownloadListFragment()
    }
}