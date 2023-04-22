package com.thewind.community.index.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.thewind.community.topic.model.TopicCardItem
import com.thewind.community.topic.model.TopicId
import com.thewind.community.topic.page.TopicCardAdapter
import com.thewind.hyper.databinding.FragmentCommunityBinding
import com.thewind.picture.main.page.PictureMainRecommendFragment
import com.thewind.torrent.search.recommend.TorrentSearchRecommendFragment
import com.thewind.viewer.h5.H5PageFragment
import com.thewind.widget.activity.FullScreenContainerActivity
import xyz.thewind.community.image.model.ImageSrc


class CommunityFragment : Fragment() {
    private lateinit var binding: FragmentCommunityBinding
    private lateinit var vm: CommunityFragmentViewModel
    private val recommendTopicList: MutableList<TopicCardItem> = mutableListOf()

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

        vm.recommendTopicLiveData.observe(viewLifecycleOwner) {
            binding.rvTopic.layoutManager =
                StaggeredGridLayoutManager(it.columnCount, StaggeredGridLayoutManager.VERTICAL)
            binding.rvTopic.adapter = TopicCardAdapter(recommendTopicList, aspectRadio = it.aspectRadio) { item ->
                if (item.actionUrl?.startsWith("http") == true) {
                    FullScreenContainerActivity.startWithFragment(
                        activity,
                        H5PageFragment.newInstance(item.actionUrl!!)
                    )
                } else if (item.topicId == TopicId.PEXELS.topic) {
                    FullScreenContainerActivity.startWithFragment(activity, PictureMainRecommendFragment.newInstance(ImageSrc.PEXELS.src))
                } else if (item.topicId == TopicId.PIXBAY.topic) {
                    FullScreenContainerActivity.startWithFragment(activity, PictureMainRecommendFragment.newInstance(ImageSrc.PIXBAY.src))
                } else if (item.topicId == TopicId.MAGNET.topic) {
                    FullScreenContainerActivity.startWithFragment(activity, TorrentSearchRecommendFragment.newInstance())
                } else if (item.topicId == TopicId.KONA.topic) {
                    FullScreenContainerActivity.startWithFragment(activity, PictureMainRecommendFragment.newInstance(ImageSrc.KONA.src))
                } else if (item.topicId == TopicId.WALLHAVEN.topic) {
                    FullScreenContainerActivity.startWithFragment(activity, PictureMainRecommendFragment.newInstance(ImageSrc.WALLHAVEN.src))
                }else if (item.topicId == TopicId.HUBBLE.topic) {
                    FullScreenContainerActivity.startWithFragment(activity, PictureMainRecommendFragment.newInstance(ImageSrc.HUBBLE.src))
                }


            }
            recommendTopicList.clear()
            recommendTopicList.addAll(it.cardList)
            binding.rvTopic.adapter?.notifyDataSetChanged()
        }
        vm.loadRecommendTopic()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CommunityFragment()
    }
}