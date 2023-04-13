package com.thewind.community.comment.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.thewind.hyper.R
import com.thewind.hyper.databinding.PopCommentDialogFragmentBinding
import com.thewind.util.fillWidth

/**
 * @author: read
 * @date: 2023/4/1 下午11:52
 * @description:
 */
class PopCommentDialogFragment: DialogFragment() {
    private lateinit var binding: PopCommentDialogFragmentBinding
    private var commentFragment: CommentFragment?= null

    private var postId: String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postId = arguments?.getString("post_id")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PopCommentDialogFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postId?.let {
            commentFragment = CommentFragment.newInstance(it)
            childFragmentManager.beginTransaction().replace(R.id.fragment_container, commentFragment!!).commitNow()
        }
        binding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

    }

    override fun onStart() {
        super.onStart()
        fillWidth()
    }

    companion object {
        fun newInstance(postId: String): PopCommentDialogFragment{
            return PopCommentDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("post_id", postId)
                }
            }
        }
    }
}