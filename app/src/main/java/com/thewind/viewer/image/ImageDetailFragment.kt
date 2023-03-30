package com.thewind.viewer.image

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.thewind.hypertorrent.databinding.FragmentImageDetailBinding
import com.thewind.viewer.image.model.ImageDetail
import java.io.File

private const val IMAGE_DETAIL = "image_detail"

class ImageDetailFragment : Fragment() {

    private lateinit var binding: FragmentImageDetailBinding

    private var imageDetail: ImageDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            imageDetail = getImageDetail(savedInstanceState)
        } else {
            arguments?.let {
                imageDetail = getImageDetail(it)
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(IMAGE_DETAIL, imageDetail)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageDetail?.title?.let {
            binding.tvTitle.text = it
        }
        imageDetail?.desc?.let {
            binding.tvImageDesc.text = it
        }
        Glide.with(binding.root).load(imageDetail?.url).fitCenter().into(binding.ivImageDetail)

        binding.ivImageDetail.setOnClickListener {
            val isVisible = binding.tvTitle.isVisible
            binding.tvTitle.visibility = if (isVisible) View.GONE else View.VISIBLE
            binding.flImageDescContainer.visibility = if (isVisible) View.GONE else View.VISIBLE
        }
    }

    private fun getImageDetail(bundle: Bundle?): ImageDetail? {
        bundle ?: return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) bundle.getParcelable(
            IMAGE_DETAIL,
            ImageDetail::class.java
        ) else bundle.getParcelable(IMAGE_DETAIL)

    }

    companion object {

        @JvmStatic
        fun newInstance(imageDetail: ImageDetail) =
            ImageDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(IMAGE_DETAIL, imageDetail)
                }
            }
    }
}