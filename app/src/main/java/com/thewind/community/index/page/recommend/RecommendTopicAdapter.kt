package com.thewind.community.index.page.recommend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thewind.base.glide.fillCenterCrop
import com.thewind.community.index.model.RecommendTopicItem
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.ImageTextItemBinding
import com.thewind.util.toast

/**
 * @author: read
 * @date: 2023/4/7 上午1:48
 * @description:
 */
class RecommendTopicAdapter(private val list: List<RecommendTopicItem>) :
    RecyclerView.Adapter<RecommendTopicViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendTopicViewHolder {
        return RecommendTopicViewHolder(
            ImageTextItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.image_text_item, parent, false
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecommendTopicViewHolder, position: Int) {
        val item = list[position]
        Glide.with(holder.binding.root).load(item.icon).into(holder.binding.ivIcon)
        holder.binding.tvDesc.text = item.title
        holder.binding.root.setOnClickListener {
            toast(item.url ?: "")
        }
    }

}

class RecommendTopicViewHolder(val binding: ImageTextItemBinding) :
    RecyclerView.ViewHolder(binding.root)