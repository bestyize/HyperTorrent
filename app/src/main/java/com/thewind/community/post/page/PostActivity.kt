package com.thewind.community.post.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.thewind.community.post.model.PostContent
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.ActivityPostBinding
import com.thewind.util.ViewUtils
import com.thewind.viewer.image.ImageViewerFragment
import java.util.Date

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private lateinit var vm: PostActivityViewModel

    private var postId: String? = null

    private var postContent: PostContent ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.enterFullScreenMode(this, true)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        postId = intent.getStringExtra("post_id")
        vm = ViewModelProvider(this)[PostActivityViewModel::class.java]
        initData()
    }

    private fun initData() {
        vm.postContentLiveData.observe(this) {content ->
            postContent = content
            binding.upName.text = postContent?.upName
            postContent?.images?.let { imageList ->
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ImageViewerFragment.newInstance(imageList, true)).commitNow()
            }
            postContent?.title?.let {
                binding.tvTitle.visibility = View.VISIBLE
                binding.tvTitle.text = it
            }

            postContent?.content?.let {
                binding.tvContent.visibility = View.VISIBLE
                binding.tvContent.text = it
            }

            postContent?.postDate?.let {
                binding.tvPostDate.text = Date().toGMTString()
            }

            postContent?.likeCount?.let {
                binding.tvLikeCount.text = it.toString()
            }

            postContent?.commentCount?.let {
                binding.tvCommentCount.text = it.toString()
            }

            postContent?.collectCount?.let {
                binding.tvCollectionCount.text = it.toString()
            }
            postContent?.upHeaderUrl?.let {
                Glide.with(binding.upHeader).load(postContent?.upHeaderUrl).circleCrop().into(binding.upHeader)
            }
        }
        postId?.let {
            vm.loadContent(it)
        }
    }
}