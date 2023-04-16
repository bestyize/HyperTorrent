package xyz.thewind.community.image.model

class ImageSearchResponse {
    var code = -1
    var msg: String? = null
    var data: MutableList<ImageInfo> = mutableListOf()
}