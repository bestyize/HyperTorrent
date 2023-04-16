package com.thewind.picture.main.page

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.thewind.hyper.R
import com.thewind.hyper.databinding.PictureMainRecommendFragemntBinding
import com.thewind.picture.main.model.ImageRecommendTab
import com.thewind.picture.search.page.ImageSearchFragment
import com.thewind.widget.activity.FullScreenContainerActivity
import xyz.thewind.community.image.model.ImageSrc

/**
 * @author: read
 * @date: 2023/4/15 下午8:21
 * @description:
 */
class PictureMainRecommendFragment: Fragment() {

    private lateinit var binding: PictureMainRecommendFragemntBinding
    private lateinit var vm: PictureMainRecommendFragmentViewModel
    private var tabs: MutableList<ImageRecommendTab> = mutableListOf()
    private var src: Int = ImageSrc.PEXELS.src

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        src = arguments?.getInt("src")?:ImageSrc.PEXELS.src
        vm = ViewModelProvider(this)[PictureMainRecommendFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PictureMainRecommendFragemntBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vpContainer.offscreenPageLimit = 5
        binding.vpContainer.adapter = PictureMainRecommendAdapter(childFragmentManager, lifecycle, tabs)
        context?.resources?.getColor(R.color.bili_pink_transport)?.let { biliPink ->
            binding.tabs.tabRippleColor = ColorStateList.valueOf(biliPink)
        }

        binding.cvSearchBg.setOnClickListener {
            FullScreenContainerActivity.startWithFragment(activity, ImageSearchFragment.newInstance(src))
        }

        vm.recommendTabs.observe(viewLifecycleOwner) {
            tabs.clear()
            tabs.addAll(it)
            TabLayoutMediator(binding.tabs, binding.vpContainer, false)  { tab, pos ->
                tab.text = tabs[pos].title
            }.attach()
            binding.vpContainer.adapter?.notifyDataSetChanged()
        }
        vm.loadRecommendTabs(src)

    }

    companion object {
        fun newInstance(src: Int) = PictureMainRecommendFragment().apply {
            arguments = Bundle().apply {
                putInt("src", src)
            }
        }
    }



}