package com.thewind.local

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.thewind.hyper.R
import com.thewind.hyper.databinding.FragmentLocalFileBinding
import com.thewind.player.detail.DetailPlayerActivity
import com.thewind.torrent.select.TorrentSelectDialogFragment
import com.thewind.util.*
import com.thewind.viewer.FileViewerActivity
import com.xunlei.download.config.BASE_DOWNLOAD_DIR
import com.xunlei.download.config.STORAGE_ROOT
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PAGE_PATH = "page_path"

class LocalFileFragment : Fragment() {
    private var path: String = BASE_DOWNLOAD_DIR
    private lateinit var binding: FragmentLocalFileBinding
    private var files: MutableList<File> = mutableListOf()
    private lateinit var vm: LocalFileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocalFileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        activity?.title = "文件"
        vm.path.value = path

    }

    private fun initView() {
        vm = ViewModelProvider(this)[LocalFileViewModel::class.java]
        arguments?.let {
            it.getString(PAGE_PATH)?.let { p ->
                path = p
            }
        }
        binding.srfRefresh.setColorSchemeColors(resources.getColor(R.color.bili_pink))
        binding.srfRefresh.setOnRefreshListener {
            binding.srfRefresh.isRefreshing = true
            vm.path.value = path
        }
        vm.clickItem.observe(viewLifecycleOwner) { pos ->
            files.clear()
            File(path).listFiles()?.let { files.addAll(it) }
            files.nameSort()
            val file = files[pos]
            when {
                file.isTorrent() -> TorrentSelectDialogFragment.newInstance(file.absolutePath)
                    .showNow(childFragmentManager, "torrent_select_$pos")
                file.isDirectory -> {
                    path = file.absolutePath
                    vm.path.value = path

                }
                else -> LocalFileUtil.openFile(activity, file)
            }
        }

        vm.path.observe(viewLifecycleOwner) {
            binding.srfRefresh.isRefreshing = false
            files.clear()
            File(path).listFiles()?.let { files.addAll(it) }
            files.nameSort()
            binding.rvLocalFileList.layoutManager = LinearLayoutManager(context)
            binding.rvLocalFileList.adapter = LocalFileAdapter(files).apply {
                this.vmm = vm
            }
            binding.folderPath.text = path.replace(STORAGE_ROOT, "")
            binding.ivNothing.visibility = if (files.size == 0) View.VISIBLE else View.GONE
        }

        vm.longClickItem.observe(viewLifecycleOwner) { pos ->
            if (pos >= files.size) return@observe
            val file = files[pos]
            LocalFileProcessDialogFragment.newInstance(file.absolutePath, action = { action, extra ->
                when (action) {
                    DialogAction.DELETE -> {
                        lifecycleScope.launch {
                            delay(200)
                            files.removeAt(pos)
                            binding.rvLocalFileList.adapter?.notifyItemRemoved(pos)
                        }
                    }
                    DialogAction.RENAME -> {
                        lifecycleScope.launch {
                            delay(200)
                            files[pos] = File(extra)
                            binding.rvLocalFileList.adapter?.notifyItemChanged(pos)
                        }
                    }
                    else -> {}
                }
            }).showNow(childFragmentManager, file.absolutePath)
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (path == STORAGE_ROOT) {
                        return
                    }
                    path = File(path).parent ?: STORAGE_ROOT
                    vm.path.value = path
                }
            })
    }


    companion object {
        @JvmStatic
        fun newInstance(path: String? = null) =
            LocalFileFragment().apply {
                arguments = Bundle().apply {
                    putString(PAGE_PATH, path)
                }
            }
    }
}