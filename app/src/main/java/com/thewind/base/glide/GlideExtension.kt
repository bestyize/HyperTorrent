package com.thewind.base.glide

import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * @author: read
 * @date: 2023/4/1 下午11:10
 * @description:
 */


fun ImageView?.fillCircle(url: String?) {
    this?:return
    this.context?:return
    Glide.with(this.context).load(url).circleCrop().into(this)
}

fun ImageView?.fillCenterCrop(url: String?) {
    this?:return
    this.context?:return
    Glide.with(this.context).load(url).centerCrop().into(this)
}