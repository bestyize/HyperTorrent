package com.thewind.community.editor.page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thewind.base.glide.fillCenterCrop
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.ItemImagePickerBinding

/**
 * @author: read
 * @date: 2023/4/2 上午2:47
 * @description:
 */
class ImagePickerAdapter(private val list: MutableList<String>): RecyclerView.Adapter<ImagePickerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePickerViewHolder {
        return ImagePickerViewHolder(ItemImagePickerBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.item_image_picker, parent, false)))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ImagePickerViewHolder, position: Int) {
        val item = list[position]
        holder.binding.ivImagePreview.fillCenterCrop(item)
    }
}

class ImagePickerViewHolder(val binding: ItemImagePickerBinding) : RecyclerView.ViewHolder(binding.root)