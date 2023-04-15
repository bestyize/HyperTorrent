package com.thewind.picture.main.page

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.thewind.hyper.R
import com.thewind.hyper.databinding.PictureMainRecommendFragemntBinding
import com.thewind.picture.main.model.PictureRecommendTab

/**
 * @author: read
 * @date: 2023/4/15 下午8:21
 * @description:
 */
class PictureMainRecommendFragment: Fragment() {

    private lateinit var binding: PictureMainRecommendFragemntBinding
    private lateinit var vm: PictureMainRecommendFragmentViewModel
    private var tabs: MutableList<PictureRecommendTab> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        vm.recommendTabs.observe(viewLifecycleOwner) {
            tabs.addAll(it)
            TabLayoutMediator(binding.tabs, binding.vpContainer, false)  { tab, pos ->
                tab.text = tabs[pos].name
            }.attach()
            binding.vpContainer.adapter?.notifyDataSetChanged()
        }
        vm.loadRecommendTabs()

    }

    companion object {
        fun newInstance() = PictureMainRecommendFragment()
    }



}