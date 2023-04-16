package com.thewind.picture.main.model

import android.os.Parcel
import android.os.Parcelable
import xyz.thewind.community.image.model.ImageSrc

class ImageRecommendTab() : Parcelable {
    var title: String? = null
    var page: Int = 1
    var num: Int = 20
    var query: String? = null
    var src: Int = ImageSrc.PEXELS.src

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        page = parcel.readInt()
        num = parcel.readInt()
        query = parcel.readString()
        src = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeInt(page)
        parcel.writeInt(num)
        parcel.writeString(query)
        parcel.writeInt(src)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageRecommendTab> {
        override fun createFromParcel(parcel: Parcel): ImageRecommendTab {
            return ImageRecommendTab(parcel)
        }

        override fun newArray(size: Int): Array<ImageRecommendTab?> {
            return arrayOfNulls(size)
        }
    }


}