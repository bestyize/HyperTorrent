package com.thewind.torrent.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.thewind.hyper.databinding.TorrentSelectDialogFragmentBinding
import com.thewind.util.fillWidth
import com.thewind.util.toJson
import com.xunlei.download.config.TorrentUtil
import com.xunlei.tool.editor.TorrentEditor
import com.xunlei.tool.editor.TorrentSimpleInfo
import kotlinx.coroutines.launch

/**
 * @author: read
 * @date: 2023/3/15 下午11:44
 * @description:
 */
class TorrentSelectDialogFragment private constructor(private val torrentFilePath: String) :
    DialogFragment() {

    private lateinit var binding: TorrentSelectDialogFragmentBinding
    private lateinit var vm: TorrentSelectViewModel

    private var torrentSimpleInfo = TorrentSimpleInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[TorrentSelectViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TorrentSelectDialogFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSubFileList.layoutManager = LinearLayoutManager(context)
        vm.torrentFileListLiveData.observe(viewLifecycleOwner) {
            torrentSimpleInfo.torrentTitle = it.torrentTitle
            torrentSimpleInfo.createDate = it.createDate
            torrentSimpleInfo.filesList.clear()
            torrentSimpleInfo.filesList.addAll(it.filesList)
            binding.resTitle.text = torrentSimpleInfo.torrentTitle
            binding.selectTitle.text = "已选择${torrentSimpleInfo.filesList.size}个项目"
            binding.rvSubFileList.adapter = TorrentSelectAdapter(torrentSimpleInfo.filesList)

        }
        binding.rbSelectAll.setOnClickListener {
            handleBatchClicked(torrentSimpleInfo)
        }
        binding.clSelectBatch.setOnClickListener {
            binding.rbSelectAll.isChecked = !binding.rbSelectAll.isChecked
            handleBatchClicked(torrentSimpleInfo)
        }
        binding.tvAddDownloadTask.setOnClickListener {
            val selectedList = torrentSimpleInfo.filesList.filter { it.isChecked }.map { it.index }.toMutableList()
            vm.addMagnetTask(torrentFilePath, selectedList)
            dismissAllowingStateLoss()
        }

        binding.closeDialog.setOnClickListener {
            dismissAllowingStateLoss()
        }

        vm.loadMagnetInfo(path = torrentFilePath)
    }

    private fun handleBatchClicked(torrentSimpleInfo: TorrentSimpleInfo) {
        val isChecked = binding.rbSelectAll.isChecked
        torrentSimpleInfo.filesList.forEachIndexed { index, torrentFileSimpleInfo ->
            run {
                if (torrentFileSimpleInfo.isChecked != isChecked) {
                    torrentFileSimpleInfo.isChecked = isChecked
                    binding.rvSubFileList.adapter?.notifyItemChanged(index)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        fillWidth()
    }

    companion object {
        fun newInstance(torrentFilePath: String): TorrentSelectDialogFragment {
            return TorrentSelectDialogFragment(torrentFilePath)
        }
    }
}