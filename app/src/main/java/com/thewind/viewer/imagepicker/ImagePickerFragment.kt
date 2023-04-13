package com.thewind.viewer.imagepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.thewind.community.editor.model.ImagePickerItem
import com.thewind.hyper.databinding.FragmentImagePickerBinding
import com.thewind.util.fillFullScreen

/**
 * @author: read
 * @date: 2023/4/6 上午1:23
 * @description:
 */
class ImagePickerFragment: DialogFragment() {

    private lateinit var binding: FragmentImagePickerBinding
    private lateinit var vm: ImagePickerViewModel
    private val imageList = mutableListOf<ImagePickerItem>()

    var action: ((String?) -> Unit)? = null

    private var selectedImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[ImagePickerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagePickerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rvItems.adapter = ImagePickerAdapter(imageList) {
            selectedImage = it.path
            dismissAllowingStateLoss()
        }

        vm.imageListLiveData.observe(viewLifecycleOwner) {
            imageList.clear()
            imageList.addAll(it)
            binding.rvItems.adapter?.notifyDataSetChanged()
        }

        vm.loadImageList()

        binding.ivClose.setOnClickListener {
            selectedImage = null
            dismissAllowingStateLoss()
        }
    }

    override fun onStart() {
        super.onStart()
        fillFullScreen()
    }

    override fun onDetach() {
        super.onDetach()
        action?.invoke(selectedImage)
    }


    companion object {
        fun newInstance(action: (String?) -> Unit = {}): ImagePickerFragment{
            return ImagePickerFragment().apply {
                this.action = action
            }
        }
    }

}