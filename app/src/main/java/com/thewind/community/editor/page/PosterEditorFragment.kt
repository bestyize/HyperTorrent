package com.thewind.community.editor.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.thewind.hypertorrent.databinding.FragmentPosterEditorBinding
import com.thewind.util.toast
import com.thewind.viewer.image.model.ImageDetail

class PosterEditorFragment : Fragment() {
    private lateinit var binding: FragmentPosterEditorBinding
    private lateinit var imagePickerVm: ImagePickerViewModel
    private val imageList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePickerVm = ViewModelProvider(this)[ImagePickerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPosterEditorBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
        binding.rvItems.adapter = ImagePickerAdapter(imageList)

        imagePickerVm.imageListLiveData.observe(viewLifecycleOwner) {
            imageList.clear()
            imageList.addAll(it)
            binding.rvItems.adapter?.notifyDataSetChanged()
        }

        imagePickerVm.loadImageList()

        imagePickerVm.publishStatusLiveData.observe(viewLifecycleOwner) {
            if (it) {
                toast("发表成功")
                activity?.onBackPressed()
            }
        }
        binding.tvPublish.setOnClickListener {
            if (binding.etContent.text.toString().isNotBlank()) {
                imagePickerVm.publish(binding.etTitle.text.toString(),binding.etContent.text.toString(), arrayListOf())
            }
        }

        binding.tvCancel.setOnClickListener {
            activity?.onBackPressed()
        }



    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PosterEditorFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}