package com.thewind.widget.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thewind.hyper.R
import com.thewind.hyper.databinding.BottomSheetItemBinding

/**
 * @author: read
 * @date: 2023/3/18 下午8:23
 * @description:
 */
class BottomSheetAdapter(private val list: MutableList<String>, private val action: (Int) -> Unit) :
    RecyclerView.Adapter<BottomSheetViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        return BottomSheetViewHolder(
            BottomSheetItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.bottom_sheet_item, parent, false
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.binding.tvItem.text = list[position]
        holder.binding.tvItem.setOnClickListener {
            action.invoke(position)
        }
    }

}

class BottomSheetViewHolder(val binding: BottomSheetItemBinding) :
    RecyclerView.ViewHolder(binding.root)