package com.thewind.download.page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thewind.download.page.model.DownloadDisplayItem
import com.thewind.download.page.util.DownloadFormat
import com.thewind.hyper.R
import com.thewind.hyper.databinding.FileDownloadItemBinding
import com.thewind.util.formatSize
import com.xunlei.downloadlib.parameter.XLConstant.XLTaskStatus
import com.xunlei.service.database.bean.DownloadTaskBean
import java.io.File

/**
 * @author: read
 * @date: 2023/3/20 上午12:23
 * @description:
 */
class DownloadListAdapter(private val list: MutableList<DownloadTaskBean>, private var onLongClickedAction: (Int) -> Unit ) :
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
        if (item.isFinished) {
            item.downloadState = XLTaskStatus.TASK_SUCCESS
        }
        holder.binding.tvDownloadState.text = DownloadFormat.formatDownloadState(item.downloadState)
        holder.binding.tvFileSize.text = "/" + item.size.formatSize()
        holder.binding.tvDownloadedSize.text = item.downloadedSize.formatSize()
        holder.binding.tvFileName.text = item.title
        holder.binding.tvDownloadSpeed.text = DownloadFormat.formatDownloadSpeed(item.downloadSpeed)
        holder.binding.downloadProgress.max = 100
        holder.binding.downloadProgress.progress = DownloadFormat.formatDownloadProgress(item.downloadedSize, item.size)
        holder.binding.root.setOnClickListener {
            onLongClickedAction.invoke(position)
        }

    }
}

class DownloadDetailListViewHolder(val binding: FileDownloadItemBinding) :
    RecyclerView.ViewHolder(binding.root)