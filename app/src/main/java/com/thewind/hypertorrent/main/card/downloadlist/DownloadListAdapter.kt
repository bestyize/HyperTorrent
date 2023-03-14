package com.thewind.hypertorrent.main.card.downloadlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thewind.hypertorrent.databinding.DownloadTaskItemBinding
import com.xunlei.download.provider.TaskInfo

/**
 * @author: read
 * @date: 2023/3/15 上午4:04
 * @description:
 */
class DownloadListAdapter(private val taskList: MutableList<TaskInfo>) : RecyclerView.Adapter<DownloadListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadListViewHolder {
        val binding = DownloadTaskItemBinding.inflate(LayoutInflater.from(parent.context))
        return DownloadListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: DownloadListViewHolder, position: Int) {
        val taskInfo = taskList[position]
        holder.binding.torrentName.text = taskInfo.magnetHash
    }
}

class DownloadListViewHolder(val binding: DownloadTaskItemBinding) : RecyclerView.ViewHolder(binding.root)