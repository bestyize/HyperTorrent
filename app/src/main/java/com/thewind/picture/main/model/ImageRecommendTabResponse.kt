package xyz.thewind.community.image.model

import com.thewind.picture.main.model.ImageRecommendTab

class ImageRecommendTabResponse {
    var code: Int = 0
    var msg: String? = null
    var data: MutableList<ImageRecommendTab> = mutableListOf()
}