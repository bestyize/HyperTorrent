package com.thewind.torrent.select

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.TorrentSelectItemBinding
import com.thewind.util.formatSize
import com.thewind.util.icon
import com.xunlei.tool.editor.TorrentFileSimpleInfo

/**
 * @author: read
 * @date: 2023/3/16 上午2:05
 * @description:
 */
class TorrentSelectAdapter(private val items: MutableList<TorrentFileSimpleInfo>) :
    RecyclerView.Adapter<TorrentSelectViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorrentSelectViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.torrent_select_item, parent, false)
        return TorrentSelectViewHolder(TorrentSelectItemBinding.bind(view))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TorrentSelectViewHolder, position: Int) {
        val item = items[position]
        holder.binding.rbSelect.isSelected = true
        holder.binding.tvResTitle.text = item.name
        val fileSize = "文件大小:${item.size.formatSize()}"
        holder.binding.tvResDesc.text = fileSize
        holder.binding.rbSelect.isChecked = item.isChecked
        item.name?.icon()?.let {
            holder.binding.ivTorrentItemIcon.setImageResource(it)
        }
        holder.binding.root.setOnClickListener {
            item.isChecked = !item.isChecked
            notifyItemChanged(position)
        }
    }
}


class TorrentSelectViewHolder(val binding: TorrentSelectItemBinding) :
    RecyclerView.ViewHolder(binding.root)