package com.thewind.community.feed.page

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thewind.community.feed.model.RecommendFeetCard
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.RecommendFeedCardBinding

/**
 * @author: read
 * @date: 2023/3/30 上午1:27
 * @description:
 */
class RecommendCardAdapter(var list: MutableList<RecommendFeetCard>, private val action: (Int) -> Unit): RecyclerView.Adapter<RecommendCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendCardViewHolder {
        return RecommendCardViewHolder(RecommendFeedCardBinding.bind(LayoutInflater.from(parent.context).inflate(
            R.layout.recommend_feed_card, parent, false)))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecommendCardViewHolder, position: Int) {
        val item = list[position]
        Glide.with(holder.binding.root).load(item.cover).placeholder(R.drawable.user_center_bg).centerCrop().into(holder.binding.ivCover)
        Glide.with(holder.binding.root).load(item.upIcon).circleCrop().into(holder.binding.ivUpHeader)
        holder.binding.tvTitle.text = item.title
        holder.binding.tvUpName.text = item.upName
        Glide.with(holder.binding.root).load(item.rightIcon).placeholder(R.drawable.like).centerCrop().into(holder.binding.ivRight)
        holder.binding.tvRight.text = item.rightText
        holder.binding.root.setOnClickListener {
            action.invoke(0)
        }

    }


}

class RecommendCardViewHolder(val binding: RecommendFeedCardBinding): RecyclerView.ViewHolder(binding.root)

