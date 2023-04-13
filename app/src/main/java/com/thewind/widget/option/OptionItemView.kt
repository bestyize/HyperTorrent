package com.thewind.widget.option

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.thewind.hyper.R
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.thewind.hyper.databinding.ItemOptionLayoutBinding

class OptionItemView(context: Context, attr: AttributeSet): ConstraintLayout(context, attr) {
    private var binding: ItemOptionLayoutBinding

    var key: String? = null
        set(value) {
            field = value
            binding.tvKey.text = key
        }

    var desc: String? = null
        set(value) {
            field = value
            binding.tvDesc.text = desc
        }

    var icon: Any? = null
        set(value) {
            field = value
            Glide.with(this).load(icon).centerCrop().into(binding.ivDescImage)
        }

    var gotoIcon: Any? = null
        set(value) {
            field = value
            Glide.with(this).load(gotoIcon).centerCrop().into(binding.ivGoto)
        }

    init {
        binding = ItemOptionLayoutBinding.bind(LayoutInflater.from(context).inflate(R.layout.item_option_layout, this, true))
        val ta = context.obtainStyledAttributes(attr, R.styleable.OptionItemView)
        binding.tvKey.text = ta.getString(R.styleable.OptionItemView_key)

        val iconResId = ta.getResourceId(R.styleable.OptionItemView_icon, -1)
        if (iconResId == -1) {
            binding.ivDescImage.visibility = View.GONE
        } else {
            binding.ivDescImage.setImageResource(iconResId)
            binding.tvDesc.visibility = View.GONE
        }
        key = ta.getString(R.styleable.OptionItemView_key)
        desc = ta.getString(R.styleable.OptionItemView_desc)
        if (desc.isNullOrEmpty()) {
            binding.tvDesc.visibility = View.GONE
        } else {
            binding.tvDesc.text = desc
        }
        binding.ivGoto.setImageResource(ta.getResourceId(R.styleable.OptionItemView_goIcon, R.drawable.right_arrow))
        ta.recycle()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }


}