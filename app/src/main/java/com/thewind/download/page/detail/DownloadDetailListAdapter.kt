package com.thewind.download.page.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thewind.download.page.model.DownloadDisplayItem
import com.thewind.download.page.util.DownloadFormat
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.FileDownloadItemBinding
import com.thewind.util.formatSize

/**
 * @author: read
 * @date: 2023/3/20 上午12:23
 * @description:
 */
class DownloadDetailListAdapter(private val list: MutableList<DownloadDisplayItem>) :
    RecyclerView.Adapter<DownloadDetailListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DownloadDetailListViewHolder {
        return DownloadDetailListViewHolder(
            FileDownloadItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.file_download_item, parent, false
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DownloadDetailListViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvDownloadState.text = DownloadFormat.formatDownloadState(item.downloadState)
        holder.binding.tvFileSize.text = "/" + item.totalSize.formatSize()
        holder.binding.tvDownloadedSize.text = item.downloadedSize.formatSize()
        holder.binding.tvFileName.text = item.fileName
        holder.binding.tvDownloadSpeed.text = DownloadFormat.formatDownloadSpeed(item.downloadSpeed)
        holder.binding.downloadProgress.max = 100
        holder.binding.downloadProgress.progress = DownloadFormat.formatDownloadProgress(item.downloadedSize, item.totalSize)

    }
}

class DownloadDetailListViewHolder(val binding: FileDownloadItemBinding) :
    RecyclerView.ViewHolder(binding.root)