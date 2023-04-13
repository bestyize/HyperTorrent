package com.thewind.community.comment.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.thewind.hyper.databinding.PopCommentEditorDialogFragmentBinding
import com.thewind.util.fillWidth
import com.thewind.util.toast

/**
 * @author: read
 * @date: 2023/4/2 上午12:53
 * @description:
 */
class PopCommentEditorDialogFragment: DialogFragment() {

    private lateinit var binding: PopCommentEditorDialogFragmentBinding
    private lateinit var vm: PopCommentEditorViewModel

    private var postId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postId = arguments?.getString("post_id")
        vm = ViewModelProvider(this)[PopCommentEditorViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        fillWidth()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PopCommentEditorDialogFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSendComment.setOnClickListener {
            postId?.let {
                vm.sendComment(it, binding.tvSendComment.text.toString())
            }
        }
        binding.tvSendComment.requestFocus()
        vm.commentStatus.observe(viewLifecycleOwner) {
            if (it) {
                toast("评论成功")
                dismissAllowingStateLoss()
            } else toast("评论失败")
        }
        
    }

    companion object {
        fun newInstance(postId: String): PopCommentEditorDialogFragment {
            return PopCommentEditorDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("post_id", postId)
                }
            }
        }
    }
}