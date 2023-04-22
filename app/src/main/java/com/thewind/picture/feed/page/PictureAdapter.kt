package com.thewind.picture.feed.page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.thewind.hyper.R
import com.thewind.hyper.databinding.GridPictureCardItemBinding
import com.thewind.picture.main.model.ImageInfo
import com.thewind.util.ViewUtils

/**
 * @author: read
 * @date: 2023/4/15 下午9:23
 * @description:
 */
class PictureAdapter(
    val list: MutableList<ImageInfo>, private val action: (ImageInfo, Int) -> Unit
) : RecyclerView.Adapter<PictureViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder =
        PictureViewHolder(
            GridPictureCardItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.grid_picture_card_item, parent, false)
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvDesc.text = item.tags
        holder.binding.tvLeftBottom.text = item.views.toString() + " 浏览"
        holder.binding.tvSize.text = "${item.width} x ${item.height}"
        val width = ViewUtils.getScreenWidth()
        val height = if (item.previewWidth == 0) {
            (width * (item.height.toFloat() / item.width.toFloat())).toInt()
        } else {
            (width * (item.previewHeight.toFloat() / item.previewWidth.toFloat())).toInt()
        }
        val lp = holder.binding.ivCover.layoutParams
        lp.width = width
        lp.height = height
        holder.binding.ivCover.layoutParams = lp
        val lazyHeader = LazyHeaders.Builder()
        item.downloadExtras.forEach { (t, u) ->
            lazyHeader.addHeader(t, u)
            if (t == "user-agent") {
                lazyHeader.setHeader("User-Agent", u)
            }
        }
        val url = GlideUrl(item.previewImageUrl, lazyHeader.build())
        Glide.with(holder.itemView).load(url).centerCrop().into(holder.binding.ivCover)

        holder.binding.root.setOnClickListener {
            action.invoke(item, 0)
        }

        holder.binding.flMore.setOnClickListener {
            action.invoke(item, 1)
        }
    }


}


class PictureViewHolder(val binding: GridPictureCardItemBinding) :
    RecyclerView.ViewHolder(binding.root)