package com.thewind.community.topic.page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thewind.community.topic.model.TopicCardItem
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.TopicCardItemBinding

/**
 * @author: read
 * @date: 2023/4/13 上午1:50
 * @description:
 */
class TopicCardAdapter(private val list:MutableList<TopicCardItem>, private val aspectRadio: Float = 0.6f, private val action:(TopicCardItem) -> Unit) : RecyclerView.Adapter<TopicCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicCardViewHolder {
        return TopicCardViewHolder(TopicCardItemBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.topic_card_item, parent, false)))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TopicCardViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvTitle.text = item.title
        holder.binding.tvDesc.text = item.desc
        holder.binding.tvLeftBottom.text = item.leftBottomText

        holder.binding.ivCover.doOnPreDraw {
            val width = holder.binding.ivCover.measuredWidth
            val height = (width * aspectRadio).toInt()
            val lp = holder.binding.ivCover.layoutParams
            lp.height = height
            holder.binding.ivCover.layoutParams = lp
            holder.binding.vCoverMask.layoutParams = lp
            Glide.with(holder.itemView).load(item.cover).centerCrop().into(holder.binding.ivCover)
        }

        holder.itemView.setOnClickListener {
            action.invoke(item)
        }
    }


}

class TopicCardViewHolder(val binding: TopicCardItemBinding): RecyclerView.ViewHolder(binding.root)