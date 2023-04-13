package com.thewind.torrent.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thewind.hyper.R
import com.thewind.hyper.databinding.TorrentSearchItemBinding
import com.thewind.torrent.search.model.TorrentInfo

/**
 * @author: read
 * @date: 2023/3/18 下午7:38
 * @description:
 */
class TorrentSearchAdapter(
    private val list: MutableList<TorrentInfo>,
    private val action: (Int) -> Unit
) :
    RecyclerView.Adapter<TorrentSearchVideHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorrentSearchVideHolder {
        return TorrentSearchVideHolder(
            TorrentSearchItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.torrent_search_item, parent, false)
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TorrentSearchVideHolder, position: Int) {
        val info = list[position]
        holder.binding.fileName.text = info.title
        holder.binding.fileSize.text = info.size
        holder.binding.fileTime.text = info.date
        holder.binding.tvIndex.text = (position + 1).toString()
        holder.binding.root.setOnClickListener {
            action.invoke(position)
        }

    }
}

class TorrentSearchVideHolder(val binding: TorrentSearchItemBinding) :
    RecyclerView.ViewHolder(binding.root)