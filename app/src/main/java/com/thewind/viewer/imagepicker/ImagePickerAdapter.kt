package com.thewind.viewer.imagepicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thewind.base.glide.fillCenterCrop
import com.thewind.community.editor.model.ImagePickerItem
import com.thewind.hyper.R
import com.thewind.hyper.databinding.ItemImagePickerBinding

/**
 * @author: read
 * @date: 2023/4/2 上午2:47
 * @description:
 */
class ImagePickerAdapter(private val list: MutableList<ImagePickerItem>, private val action: (ImagePickerItem) -> Unit = {}): RecyclerView.Adapter<ImagePickerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePickerViewHolder {
        return ImagePickerViewHolder(ItemImagePickerBinding.bind(LayoutInflater.from(parent.context).inflate(R.layout.item_image_picker, parent, false)))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ImagePickerViewHolder, position: Int) {
        val item = list[position]
        holder.binding.ivImagePreview.fillCenterCrop(item.path)
        holder.binding.cbCheckbutton.isChecked = item.isChecked
        holder.binding.root.setOnClickListener {
            holder.binding.cbCheckbutton.isChecked = !holder.binding.cbCheckbutton.isChecked
            item.isChecked = holder.binding.cbCheckbutton.isChecked
            action.invoke(item)
        }
        holder.binding.cbCheckbutton.checkChangeListener = { isChecked ->
            item.isChecked = holder.binding.cbCheckbutton.isChecked
        }
    }
}

class ImagePickerViewHolder(val binding: ItemImagePickerBinding) : RecyclerView.ViewHolder(binding.root)