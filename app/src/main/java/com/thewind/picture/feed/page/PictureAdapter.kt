package com.thewind.picture.feed.page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thewind.hyper.R
import com.thewind.hyper.databinding.GridPictureCardItemBinding
import com.thewind.util.ViewUtils
import xyz.thewind.community.image.model.ImageInfo

/**
 * @author: read
 * @date: 2023/4/15 下午9:23
 * @description:
 */
class PictureAdapter(val list: MutableList<ImageInfo>, private val action: (ImageInfo, Int) -> Unit): RecyclerView.Adapter<PictureViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder = PictureViewHolder(GridPictureCardItemBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.grid_picture_card_item, parent, false)))
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvDesc.text = item.tags
        holder.binding.tvLeftBottom.text = item.views.toString() + " 浏览"

        val width = ViewUtils.getScreenWidth()
        val height = (width * (item.height.toFloat() / item.width.toFloat())).toInt()
        val lp = holder.binding.ivCover.layoutParams
        lp.width = width
        lp.height = height
        holder.binding.ivCover.layoutParams = lp
        Glide.with(holder.itemView).load(item.previewImageUrl).centerCrop().into(holder.binding.ivCover)

        holder.binding.root.setOnClickListener {
            action.invoke(item, 0)
        }

        holder.binding.flMore.setOnClickListener {
            action.invoke(item, 1)
        }
    }


}


class PictureViewHolder(val binding: GridPictureCardItemBinding) : RecyclerView.ViewHolder(binding.root)