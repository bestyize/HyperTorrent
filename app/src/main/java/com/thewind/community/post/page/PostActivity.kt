package com.thewind.community.post.page

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.thewind.community.comment.page.PopCommentDialogFragment
import com.thewind.community.comment.page.PopCommentEditorDialogFragment
import com.thewind.community.post.model.PostContent
import com.thewind.community.user.UserDetailActivity
import com.thewind.hyper.R
import com.thewind.hyper.databinding.ActivityPostBinding
import com.thewind.user.login.AccountHelper
import com.thewind.util.ViewUtils
import com.thewind.util.toJson
import com.thewind.util.toast
import com.thewind.viewer.image.ImageViewerFragment
import java.util.*


private const val TAG = "PostActivity"
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
            binding.upName.text = postContent?.userName
            if (postContent?.images?.isEmpty() == true) {
                binding.fragmentContainer.visibility = View.GONE
            }
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

            content?.uid?.let {
                val isOwner = AccountHelper.loadUserInfo().uid == it || AccountHelper.loadUserInfo().level >= 100
                if (isOwner) {
                    binding.tvDelete.visibility = View.VISIBLE
                }
                binding.tvDelete.setOnClickListener {
                    content.id?.let { postId ->
                        vm.delete(postId)
                    }
                }
                vm.deletePostLiveData.observe(this@PostActivity) {
                    if (it.data) {
                        toast("删除成功")
                        onBackPressed()
                    }
                }

            }

            binding.ivLike.setOnClickListener {
                vm.like(content.id?:"-1")
            }
            vm.likeLiveData.observe(this@PostActivity) {
                Log.i(TAG, "likeData = ${it.toJson()}")
                if (it.data) {
                    content.likeCount++
                    binding.tvLikeCount.text = content.likeCount.toString()
                }
            }

            binding.ivCollect.setOnClickListener {
                vm.collect(content.id ?:"-1")
            }
            vm.collectLiveData.observe(this@PostActivity) {
                if (it.data) {
                    content.collectCount++
                    binding.tvCollectionCount.text = content.collectCount.toString()
                }
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
            postContent?.userHeaderUrl?.let {
                Glide.with(binding.upHeader).load(postContent?.userHeaderUrl).circleCrop()
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
                intent.putExtra("uid", content.uid)
                startActivity(intent)

            }
            binding.tvFollow.text = if (content.follow) "已关注" else "关注"
            vm.attentionLiveData.observe(this@PostActivity) {
                binding.tvFollow.text = if (it.follow) "已关注" else "关注"
            }
            binding.tvFollow.setOnClickListener {
                if (vm.attentionLiveData.value?.follow == true) {
                    vm.attentionUser(content.uid, query = false, follow = false)
                } else {
                    vm.attentionUser(content.uid, query = false, follow = true)
                }
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