package com.thewind.download.page.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thewind.download.page.util.DownloadFormat
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.FileDownloadItemBinding
import com.thewind.util.formatSize
import com.thewind.util.isVideo
import com.xunlei.downloadlib.parameter.XLConstant.XLTaskStatus
import com.xunlei.service.database.bean.DownloadFileItemBean
import java.io.File

/**
 * @author: read
 * @date: 2023/3/20 上午12:23
 * @description:
 */
class DownloadDetailListAdapter(private val list: MutableList<DownloadFileItemBean>,
                                private val action: (Int, Int) -> Unit) :
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
        if (item.isFinished) item.downloadState = XLTaskStatus.TASK_SUCCESS
        holder.binding.tvDownloadState.text = DownloadFormat.formatDownloadState(item.downloadState)
        holder.binding.tvFileSize.text = "/" + item.size.formatSize()
        holder.binding.tvDownloadedSize.text = item.downloadedSize.formatSize()
        holder.binding.tvFileName.text = item.name
        holder.binding.tvDownloadSpeed.text = if (item.isFinished) "" else DownloadFormat.formatDownloadSpeed(item.downloadSpeed)
        holder.binding.downloadProgress.max = 100
        holder.binding.downloadProgress.progress = DownloadFormat.formatDownloadProgress(item.downloadedSize, item.size)
        holder.binding.root.setOnClickListener {
            if (item.isFinished) {
                action.invoke(position, Action.OPEN_FILE.action)
            } else if (item.downloadState == XLTaskStatus.TASK_RUNNING) {
                action.invoke(position, Action.PAUSE_DOWNLOAD.action)
            } else if (item.downloadState == XLTaskStatus.TASK_IDLE) {
                action.invoke(position, Action.START_DOWNLOAD.action)
            }
        }

    }
}
internal enum class Action(val action: Int) {
    OPEN_FILE(0),
    PAUSE_DOWNLOAD(1),
    START_DOWNLOAD(2),
}

class DownloadDetailListViewHolder(val binding: FileDownloadItemBinding) :
    RecyclerView.ViewHolder(binding.root)