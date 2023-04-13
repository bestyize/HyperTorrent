package com.thewind.viewer.image

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.thewind.hyper.databinding.ImageViewerFragmentBinding
import com.thewind.util.ViewUtils
import com.thewind.util.isPicture
import com.thewind.viewer.image.model.ImageDetail
import java.io.File

/**
 * @author: read
 * @date: 2023/3/31 上午12:15
 * @description:
 */

private const val IMAGE_LIST = "image_list"
private const val INNER_MODE = "inner_mode"

class ImageViewerFragment : Fragment() {

    private var imageList: java.util.ArrayList<ImageDetail>? = null
    private var innerMode: Boolean = true

    private lateinit var binding: ImageViewerFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageList = getImageDetail(arguments)
        innerMode = arguments?.getBoolean(INNER_MODE, true)?:true
        ViewUtils.enterFullScreenMode(activity, innerMode)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ImageViewerFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vpContainer.offscreenPageLimit = 5
        binding.vpContainer.adapter =
            ImageDetailAdapter(childFragmentManager, lifecycle, imageList ?: mutableListOf(), innerMode)
        TabLayoutMediator(binding.tbImages, binding.vpContainer, false) { tab, pos ->
            tab.text = ""
        }.attach()
    }


    private fun getImageDetail(bundle: Bundle?): java.util.ArrayList<ImageDetail>? {
        bundle ?: return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) bundle.getParcelableArrayList(
            IMAGE_LIST, ImageDetail::class.java
        ) else bundle.getParcelable(IMAGE_LIST)

    }

    companion object {

        @JvmStatic
        fun newInstance(list: ArrayList<ImageDetail>, innerMode: Boolean): Fragment {
            return ImageViewerFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(IMAGE_LIST, list)
                    putBoolean(INNER_MODE, innerMode)
                }
            }
        }
        @JvmStatic
        fun newInstance(path: String): Fragment {
            val arrList = ArrayList<ImageDetail>()
            val file = File(path)
            if (file.isFile && file.exists()) {
                arrList.add(ImageDetail().apply {
                    url = "file://" + file.absolutePath
                    desc = file.name
                })
                file.parentFile?.listFiles()?.filter { it.isFile && it.exists() && it.isPicture() }?.forEach {
                    arrList.add(ImageDetail().apply {
                        url = "file://" + it.absolutePath
                        desc = it.name
                    })
                }
            } else {
                file.listFiles()?.filter { it.isFile && it.exists() && it.isPicture() }?.forEach {
                    arrList.add(ImageDetail().apply {
                        url = "file://" + it.absolutePath
                        desc = it.name
                    })
                }
            }

            return ImageViewerFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(IMAGE_LIST, arrList)
                }
            }
        }
    }


}