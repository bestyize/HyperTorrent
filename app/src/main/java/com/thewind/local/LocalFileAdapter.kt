package com.thewind.local


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.LocalFileItemBinding
import com.thewind.util.*
import java.io.File

/**
 * @author: read
 * @date: 2023/3/17 上午12:44
 * @description:
 */
class LocalFileAdapter(private val files: MutableList<File>) :
    RecyclerView.Adapter<LocalFileViewHolder>() {

    var vmm: LocalFileViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalFileViewHolder {
        return LocalFileViewHolder(
            LocalFileItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.local_file_item, parent, false)
            )
        )
    }

    override fun getItemCount(): Int {
        return files.size
    }

    override fun onBindViewHolder(holder: LocalFileViewHolder, position: Int) {
        val file = files[position]
        holder.binding.fileName.text = file.name
        holder.binding.fileSize.text = file.formatSize()
        holder.binding.fileTime.text = file.formatDate()
        holder.binding.fileIcon.setImageResource(file.icon())
        if (file.isPicture()) {
            holder.binding.fileIcon.setPadding(5.toPx())
            Glide.with(holder.binding.fileIcon).load(file).placeholder(file.icon()).fitCenter().into(holder.binding.fileIcon)
        }
        holder.binding.root.setOnClickListener {
            vmm?.clickItem?.value = position
        }
        holder.binding.root.setOnLongClickListener {
            vmm?.longClickItem?.value = position
            true
        }

    }
}

class LocalFileViewHolder(val binding: LocalFileItemBinding) : RecyclerView.ViewHolder(binding.root)
