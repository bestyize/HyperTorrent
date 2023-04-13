package com.thewind.community.comment.page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thewind.base.glide.fillCircle
import com.thewind.community.comment.model.Comment
import com.thewind.hyper.R
import com.thewind.hyper.databinding.CommentItemDetailBinding

/**
 * @author: read
 * @date: 2023/4/1 下午11:06
 * @description:
 */
class CommentAdapter(private val list: List<Comment>, private val action: (Int, Comment) -> Unit): RecyclerView.Adapter<CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(CommentItemDetailBinding.bind(LayoutInflater.from(parent.context).inflate(
            R.layout.comment_item_detail, parent, false)))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = list[position]
        holder.binding.ivHeader.fillCircle(item.upHeaderUrl)
        holder.binding.tvCommentContent.text = item.content
        holder.binding.tvUpName.text = item.upName
        holder.binding.tvLikeCount.text = item.likeCount.toString()
        holder.binding.ivHeader.setOnClickListener {
            action.invoke(0, item)
        }

    }
}

class CommentViewHolder(val binding: CommentItemDetailBinding) : RecyclerView.ViewHolder(binding.root)