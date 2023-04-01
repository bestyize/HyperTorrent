package com.thewind.community.comment.page

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thewind.community.comment.model.Comment
import com.thewind.community.user.UserDetailActivity
import com.thewind.hypertorrent.databinding.FragmentCommentBinding
private const val POST_ID = "post_id"
class CommentFragment : Fragment() {
    private var postId: String? = null
    private var isLoading: Boolean = false
    private var currPage = 1
    private val commentList: MutableList<Comment> = mutableListOf()
    private lateinit var binding: FragmentCommentBinding
    private lateinit var vm: CommentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getString(POST_ID)
        }
        vm = ViewModelProvider(this)[CommentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.layoutManager = LinearLayoutManager(context)
        binding.rvItems.adapter = CommentAdapter(commentList) { action, comment ->
            when(action) {
                0 -> {
                    val intent = Intent(activity, UserDetailActivity::class.java)
                    intent.putExtra("uid", comment.upId)
                    startActivity(intent)
                }
            }
        }
        binding.rvItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var lastPos: Int = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val id = postId ?: return
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastPos == commentList.size - 1 && !isLoading) {
                        isLoading = true
                        vm.loadCommentList(id, currPage++)
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
        vm.commentLiveData.observe(viewLifecycleOwner) {
            isLoading = false
            val prevItem = commentList.size
            binding.commentTitle.text = "${it.size}条评论"
            commentList.addAll(it)
            binding.rvItems.adapter?.notifyItemRangeInserted(prevItem, it.size)
        }

        postId?.let {
            isLoading = true
            vm.loadCommentList(it, currPage++)
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(postId: String) =
            CommentFragment().apply {
                arguments = Bundle().apply {
                   putString(POST_ID, postId)
                }
            }
    }
}