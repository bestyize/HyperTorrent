package com.thewind.torrent.local

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.thewind.hypertorrent.databinding.LocalFileProcessDialogFragmentBinding
import com.thewind.util.fillWidth
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
            File(path).delete()
            dismissAllowingStateLoss()
            action.invoke(DialogAction.DELETE)
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
    }

    override fun onStart() {
        super.onStart()
        fillWidth()
    }

    companion object {
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
    DELETE(2)
}