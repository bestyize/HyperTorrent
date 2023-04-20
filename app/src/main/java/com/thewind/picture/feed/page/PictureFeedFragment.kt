package com.thewind.picture.feed.page

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thewind.hyper.databinding.FragmentPictureFeedBinding
import com.thewind.util.toast
import com.thewind.viewer.image.ImageViewerFragment
import com.thewind.viewer.image.model.ImageDetail
import com.thewind.widget.activity.FullScreenContainerActivity
import com.thewind.widget.bottomsheet.CommonBottomSheetDialogFragment
import com.thewind.picture.main.model.ImageInfo
import com.thewind.picture.main.model.ImageRecommendTab
import java.io.File
import java.lang.Exception


class PictureFeedFragment : Fragment() {

    private lateinit var binding: FragmentPictureFeedBinding
    private lateinit var vm: PictureFeedFragmentViewModel

    private var request: ImageRecommendTab = ImageRecommendTab()
    private val imageList: MutableList<ImageInfo> = mutableListOf()
    private var isLoading: Boolean = true
    private val operationList = mutableListOf("分享", "下载", "取消")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[PictureFeedFragmentViewModel::class.java]
        val req: ImageRecommendTab? = arguments?.getParcelable("request")
        if (req != null) {
            request = req
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPictureFeedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.layoutManager = LinearLayoutManager(context)
        binding.rvItems.adapter = PictureAdapter(imageList) { image, type ->
            if (type == 0) {
                FullScreenContainerActivity.startWithFragment(activity, ImageViewerFragment.newInstance(
                    arrayListOf(
                        ImageDetail().apply {
                            this.title = image.tags
                            this.desc = image.tags
                            this.url = image.imageUrl
                            this.downloadExtras.putAll(image.downloadExtras)
                            this.previewImageUrl = image.previewImageUrl
                        }
                    ), false
                ))
            } else if (type == 1) {
                CommonBottomSheetDialogFragment.newInstance(operationList) {
                    if (it == 0) {
                        vm.downloadPicture(image.imageUrl, extra = image.downloadExtras)
                    } else if (it == 1) {
                        vm.downloadPicture(image.imageUrl, true, extra = image.downloadExtras)
                    }

                }.showNow(childFragmentManager, image.hashCode().toString())
            }

        }

        binding.srfRefresh.setOnRefreshListener {
            if (!isLoading) {
                isLoading = true
                binding.srfRefresh.isRefreshing = true
                request.page = 1
                vm.loadPictureList(request.query, request.page, request.num, request.src)
            }
        }
        binding.rvItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var lastPos: Int = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastPos == imageList.size - 1 && !isLoading) {
                        isLoading = true
                        request.page++
                        vm.loadPictureList(keyword = request.query, page = request.page, num = request.num, src = request.src)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastPos =
                    (recyclerView.layoutManager as? LinearLayoutManager)?.findLastCompletelyVisibleItemPosition()
                        ?: 0
            }
        })

        vm.list.observe(viewLifecycleOwner) {
            isLoading = false
            binding.srfRefresh.isRefreshing = false
            if (request.page == 1) {
                imageList.clear()
            }
            val prevSize = imageList.size
            val afterSize = prevSize + it.data.size
            imageList.addAll(it.data)
            binding.rvItems.adapter?.notifyItemRangeChanged(prevSize, afterSize - 1)

        }
        vm.downloadState.observe(viewLifecycleOwner) {
            toast(if (it) "下载成功" else "下载失败")
        }

        vm.shareState.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                toast("分享失败")
            } else {
                try {
                    Intent(
                        Intent.ACTION_VIEW,
                        FileProvider.getUriForFile(
                            requireContext(),
                            "com.thewind.hyper.provider",
                            File(it)
                        )
                    ).apply {
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        activity?.startActivity(this)
                    }
                } catch (_: Exception) {
                    toast("分享失败")
                }

            }
        }

        vm.loadPictureList(keyword = request.query, page = request.page, num = request.num, src = request.src)

    }

    companion object {

        @JvmStatic
        fun newInstance(request: ImageRecommendTab): PictureFeedFragment {
            return PictureFeedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("request", request)
                }
            }
        }
    }
}