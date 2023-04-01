package com.thewind.community.post.page

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.thewind.community.comment.page.PopCommentDialogFragment
import com.thewind.community.comment.page.PopCommentEditorDialogFragment
import com.thewind.community.post.model.PostContent
import com.thewind.community.user.UserDetailActivity
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.ActivityPostBinding
import com.thewind.user.center.UserCenterFragment
import com.thewind.util.ViewUtils
import com.thewind.viewer.image.ImageViewerFragment
import java.util.*

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private lateinit var vm: PostActivityViewModel

    private var postId: String? = null

    private var postContent: PostContent? = null
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
        vm.postContentLiveData.observe(this) { content ->
            postContent = content
            binding.upName.text = postContent?.upName
            postContent?.images?.let { imageList ->
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    ImageViewerFragment.newInstance(imageList, true)
                ).commitNow()
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
                Glide.with(binding.upHeader).load(postContent?.upHeaderUrl).circleCrop()
                    .into(binding.upHeader)
            }
            binding.llCommentArea.setOnClickListener {
                postId?.let {
                    PopCommentDialogFragment.newInstance(it).showNow(supportFragmentManager, it)
                }

            }

            binding.tvPostComment.setOnClickListener {
                postId?.let {
                    PopCommentEditorDialogFragment.newInstance(it)
                        .showNow(supportFragmentManager, it)
                }
            }

            binding.upHeader.setOnClickListener {
                val intent = Intent(this@PostActivity, UserDetailActivity::class.java)
                intent.putExtra("uid", content.upId)
                startActivity(intent)

            }
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
        postId?.let {
            vm.loadContent(it)
        }
    }
}