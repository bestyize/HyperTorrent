package xyz.thewind.community.image.model

import com.thewind.picture.main.model.ImageInfo

class ImageSearchResponse {
    var code = -1
    var msg: String? = null
    var data: MutableList<ImageInfo> = mutableListOf()
}