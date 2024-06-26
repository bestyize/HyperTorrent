package com.thewind.viewer.image.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author: read
 * @date: 2023/3/31 上午1:05
 * @description:
 */
class ImageDetail() : Parcelable {
    var url: String? = null
    var previewImageUrl: String? = null
    var title: String? = null
    var desc: String? = null

    var style:Int = 0

    var downloadExtras: MutableMap<String, String> = mutableMapOf()

    constructor(parcel: Parcel) : this() {
        url = parcel.readString()
        previewImageUrl = parcel.readString()
        title = parcel.readString()
        desc = parcel.readString()
        style = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(previewImageUrl)
        parcel.writeString(title)
        parcel.writeString(desc)
        parcel.writeInt(style)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageDetail> {
        override fun createFromParcel(parcel: Parcel): ImageDetail {
            return ImageDetail(parcel)
        }

        override fun newArray(size: Int): Array<ImageDetail?> {
            return arrayOfNulls(size)
        }
    }


}

enum class ImageDisplayStyle(val style: Int) {
    FIT_CENTER(0),
    FIT_XY(1),
    CENTER_CROP(2)
}