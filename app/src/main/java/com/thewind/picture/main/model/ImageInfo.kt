package com.thewind.picture.main.model

import androidx.annotation.Keep

@Keep
class ImageInfo {
    var src: Int = 0
    var id: String? = null
    var imageUrl: String? = null
    var width: Int = 0
    var height: Int = 0
    var previewImageUrl: String? = null
    var previewWidth: Int = 0
    var previewHeight: Int = 0
    var type: String = "photo"
    var tags: String? = null
    var pageUrl: String? = null
    var views: Long = 0
    var downloads: Long = 0
    var collections: Long = 0
    var comments: Long = 0
    var likes: Long = 0
    var downloadExtras = mutableMapOf<String, String>()
}