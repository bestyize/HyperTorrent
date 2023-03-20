package com.thewind.local

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.thewind.hypertorrent.databinding.LocalFileProcessDialogFragmentBinding
import com.thewind.util.fillWidth
import com.thewind.util.isTorrent
import com.thewind.util.toast
import com.thewind.widget.inputdialog.InputDialogFragment
import com.thewind.widget.inputdialog.InputDialogModel
import com.xunlei.tool.editor.TorrentEditor
import org.apache.commons.io.FileUtils
import java.io.File


/**
 * @author: read
 * @date: 2023/3/17 下午11:22
 * @description:
 */
class LocalFileProcessDialogFragment private constructor(
    private val path: String,
    private val action: (DialogAction) -> Unit
) : DialogFragment() {

    private lateinit var binding: LocalFileProcessDialogFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocalFileProcessDialogFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvCancel.setOnClickListener {
            dismissAllowingStateLoss()
            action.invoke(DialogAction.CANCEL)
        }
        binding.tvDelete.setOnClickListener {
            Log.i(TAG, "delete file , path = $path")
            Runtime.getRuntime().exec("rm -rf $path")
            dismissAllowingStateLoss()
            action.invoke(DialogAction.DELETE)
        }
        binding.tvRename.setOnClickListener {
            dismissAllowingStateLoss()
            InputDialogFragment.newInstance(InputDialogModel().apply {
                this.title = "重命名"
                this.preInput = File(path).name
            }){ pos, content ->
                if (pos == 1 && content.isNotBlank()) {
                    val file = File(path)
                    val newName = file.absolutePath.replace(file.name, content)
                    file.renameTo(File(newName))
                    action.invoke(DialogAction.RENAME)
                }
            }.showNow(parentFragmentManager, path)
        }
        binding.tvShare.setOnClickListener {
            Intent(
                Intent.ACTION_VIEW,
                FileProvider.getUriForFile(
                    requireContext(),
                    "com.thewind.hypertorrent.provider",
                    File(path)
                )
            ).apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(this)
            }
            dismissAllowingStateLoss()
            action.invoke(DialogAction.SHARE)
        }
        if (File(path).isTorrent()) {
            binding.llWashTorrent.visibility = View.VISIBLE
        }
        binding.llWashTorrent.setOnClickListener {
            dismissAllowingStateLoss()
            val ret = TorrentEditor.washTorrentFile(path)
            toast("种子洗白${if (ret) "成功" else "失败"}")
        }
    }

    override fun onStart() {
        super.onStart()
        fillWidth()
    }

    companion object {

        private const val TAG = "LocalFileProcessDialogFragment"
        fun newInstance(
            path: String,
            action: (DialogAction) -> Unit
        ): LocalFileProcessDialogFragment {
            return LocalFileProcessDialogFragment(path, action)
        }
    }
}

enum class DialogAction(val action: Int) {
    CANCEL(0),
    SHARE(1),
    DELETE(2),
    RENAME(3)
}