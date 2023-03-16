package com.thewind.torrent.select

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.TorrentSelectDialogFragmentBinding
import com.xunlei.tool.editor.TorrentEditor
import com.xunlei.tool.editor.TorrentSimpleInfo
import kotlinx.coroutines.launch

/**
 * @author: read
 * @date: 2023/3/15 下午11:44
 * @description:
 */
class TorrentSelectDialogFragment private constructor(private val torrentFilePath: String): DialogFragment() {

    private lateinit var binding: TorrentSelectDialogFragmentBinding

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
        lifecycleScope.launch {
            val torrentSimpleInfo = TorrentEditor.parseTorrentFile(torrentFilePath)
            binding.resTitle.text = torrentSimpleInfo.torrentTitle
            binding.selectTitle.text = "已选择${torrentSimpleInfo.filesList.size}个项目"
            binding.rvSubFileList.adapter = TorrentSelectAdapter(torrentSimpleInfo.filesList)
            binding.rbSelectAll.setOnClickListener {
                handleBatchClicked(torrentSimpleInfo)
            }
            binding.clSelectBatch.setOnClickListener {
                binding.rbSelectAll.isChecked = !binding.rbSelectAll.isChecked
                handleBatchClicked(torrentSimpleInfo)
            }
        }
        binding.closeDialog.setOnClickListener {
            dismissAllowingStateLoss()
        }
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
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.gravity = Gravity.BOTTOM
        dialog?.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    companion object {
        fun newInstance(torrentFilePath: String) : TorrentSelectDialogFragment{
            return TorrentSelectDialogFragment(torrentFilePath)
        }
    }
}