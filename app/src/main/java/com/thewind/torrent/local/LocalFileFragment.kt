package com.thewind.torrent.local

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.thewind.hypertorrent.databinding.FragmentLocalFileBinding
import com.thewind.torrent.select.TorrentSelectDialogFragment
import com.thewind.util.isTorrent
import com.xunlei.download.config.STORAGE_ROOT
import com.xunlei.download.config.TORRENT_DIR
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PAGE_PATH = "page_path"

class LocalFileFragment : Fragment() {
    private var path: String = TORRENT_DIR
    private lateinit var binding: FragmentLocalFileBinding
    private var files: MutableList<File> = mutableListOf()
    private lateinit var vm: LocalFileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[LocalFileViewModel::class.java]
        arguments?.let {
            it.getString(PAGE_PATH)?.let {
                path = it
            }
        }
        vm.clickItem.observe(this) {
            val file = files[it]
            when {
                file.isTorrent() -> TorrentSelectDialogFragment.newInstance(file.absolutePath).showNow(childFragmentManager, "torrent_select_$it")
                file.isDirectory -> {
                    path = file.absolutePath
                    vm.path.value = path
                }
                else -> {
                    Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(requireContext(), "com.thewind.hypertorrent.provider", file)).apply {
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivity(this)
                    }

                }
            }
        }

        vm.path.observe(this) {
            files.clear()
            File(path).listFiles()?.let { files.addAll(it) }
            binding.rvLocalFileList.layoutManager = LinearLayoutManager(context)
            binding.rvLocalFileList.adapter = LocalFileAdapter(files).apply {
                this.vmm = vm
            }
            binding.folderPath.text = path.replace(STORAGE_ROOT, "")
        }

        activity?.onBackPressedDispatcher?.addCallback(this, object :OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (path == STORAGE_ROOT) {
                    return
                }
                path = File(path).parent?: STORAGE_ROOT
                vm.path.value = path
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocalFileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "文件"
        vm.path.value = path

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