package com.thewind.community.index.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.thewind.community.index.model.RecommendTopicItem
import com.thewind.community.index.page.recommend.RecommendTopicAdapter
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.FragmentCommunityBinding
import com.thewind.hypertorrent.databinding.FragmentRecommendFeedBinding


class CommunityFragment : Fragment() {
    private lateinit var binding: FragmentCommunityBinding
    private lateinit var vm: CommunityFragmentViewModel
    private val recommendTopicList: MutableList<RecommendTopicItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[CommunityFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRecommendTopics.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        binding.rvRecommendTopics.adapter = RecommendTopicAdapter(recommendTopicList)

        vm.recommendTopicLiveData.observe(viewLifecycleOwner) {
            recommendTopicList.clear()
            recommendTopicList.addAll(it)
            binding.rvRecommendTopics.adapter?.notifyDataSetChanged()
        }
        vm.loadRecommendTopic()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CommunityFragment()
    }
}