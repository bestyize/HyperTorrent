package com.thewind.viewer.image

import android.app.WallpaperManager
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.thewind.hyper.databinding.FragmentImageDetailBinding
import com.thewind.util.toPx
import com.thewind.util.toast
import com.thewind.viewer.image.model.ImageDetail
import com.thewind.viewer.image.model.ImageDisplayStyle
import com.thewind.widget.bottomsheet.CommonBottomSheetDialogFragment

private const val IMAGE_DETAIL = "image_detail"
private const val INNER_MODE = "inner_mode"

class ImageDetailFragment : Fragment() {

    private lateinit var binding: FragmentImageDetailBinding

    private var imageDetail: ImageDetail? = null
    private var innerMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageDetail = getImageDetail(it)
            innerMode = it.getBoolean(INNER_MODE, false)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (innerMode) {
            binding.tvTitle.minHeight = 30.toPx()
            binding.tvTitle.gravity = Gravity.CENTER
            binding.flImageDescContainer.minimumHeight = 30.toPx()
        }
        imageDetail?.title?.let {
            binding.tvTitle.text = it
        }
        imageDetail?.desc?.let {
            binding.tvImageDesc.text = it
        }
        val lazyHeader = LazyHeaders.Builder()
        imageDetail?.downloadExtras?.forEach { (t, u) ->
            lazyHeader.addHeader(t, u)
            if (t == "user-agent") {
                lazyHeader.setHeader("User-Agent", u)
            }
        }
        val url: Any? = if (imageDetail?.url?.startsWith("http") == true) GlideUrl(imageDetail?.url, lazyHeader.build()) else imageDetail?.url
        when (imageDetail?.style ?: 0) {
            ImageDisplayStyle.CENTER_CROP.style -> {
                binding.ivImageDetail.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(binding.root).load(url).centerCrop().into(binding.ivImageDetail)
            }

            else -> {
                binding.ivImageDetail.scaleType = ImageView.ScaleType.FIT_CENTER
                Glide.with(binding.root).load(url).fitCenter().into(binding.ivImageDetail)
            }
        }


        binding.ivImageDetail.setOnClickListener {
            val isVisible = binding.tvTitle.isVisible
            binding.tvTitle.visibility = if (isVisible) View.GONE else View.VISIBLE
            binding.flImageDescContainer.visibility = if (isVisible) View.GONE else View.VISIBLE
            binding.cvSetWallpaper.visibility = if (isVisible) View.GONE else View.VISIBLE
        }

        binding.tvSetToWallpaper.setOnClickListener {
            try {
                val options =
                    mutableListOf("设置为桌面壁纸", "设置为锁屏壁纸", "同时设置为桌面和锁屏壁纸")
                val bm = (binding.ivImageDetail.drawable as BitmapDrawable).bitmap
                CommonBottomSheetDialogFragment.newInstance(options) {
                    when (it) {
                        0 -> WallpaperManager.getInstance(context)
                            .setBitmap(bm, null, true, WallpaperManager.FLAG_SYSTEM)

                        1 -> WallpaperManager.getInstance(context)
                            .setBitmap(bm, null, true, WallpaperManager.FLAG_LOCK)

                        2 -> WallpaperManager.getInstance(context).setBitmap(bm, null, true)
                    }
                    toast("设置成功")
                }.showNow(childFragmentManager, "wall_papper")

            } catch (_: Exception) {
                toast("设置失败")
            }

        }
    }

    private fun getImageDetail(bundle: Bundle?): ImageDetail? {
        bundle ?: return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) bundle.getParcelable(
            IMAGE_DETAIL, ImageDetail::class.java
        ) else bundle.getParcelable(IMAGE_DETAIL)

    }

    companion object {

        @JvmStatic
        fun newInstance(imageDetail: ImageDetail) = ImageDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(IMAGE_DETAIL, imageDetail)
            }
        }
    }
}