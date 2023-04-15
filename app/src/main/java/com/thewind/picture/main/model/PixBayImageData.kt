package com.thewind.picture.main.model

import androidx.annotation.Keep

/**
 * @author: read
 * @date: 2023/4/15 下午8:50
 * @description:
 */
@Keep
class PixBayImageData {
    var id: Long = 0
    var type: String = "photo"
    var tags: String? = null
    var pageUrl: String? = null
    var imageUrl: String ?= null
    var previewImageUrl: String ?= null
    var width: Int = 0
    var height: Int = 0
    var views: Long = 0
    var downloads: Long = 0
    var collection: Long = 0
    var likes: Long = 0

}