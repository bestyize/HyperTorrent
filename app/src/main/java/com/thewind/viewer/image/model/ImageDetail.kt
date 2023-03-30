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
    var title: String? = null
    var desc: String? = null

    constructor(parcel: Parcel) : this() {
        url = parcel.readString()
        title = parcel.readString()
        desc = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(title)
        parcel.writeString(desc)
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