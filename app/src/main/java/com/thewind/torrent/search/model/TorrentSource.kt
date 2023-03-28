package com.thewind.torrent.search.model

import android.os.Parcel
import android.os.Parcelable

class TorrentSource() : Parcelable {
    var src: Int = 0
    var title: String = ""
    var subTitle: String = ""
    var desc: String = ""
    var officialUrl: String ?= null
    var level = 0

    constructor(parcel: Parcel) : this() {
        src = parcel.readInt()
        title = parcel.readString() ?: ""
        subTitle = parcel.readString() ?:""
        desc = parcel.readString() ?:""
        officialUrl = parcel.readString()
        level = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(src)
        parcel.writeString(title)
        parcel.writeString(subTitle)
        parcel.writeString(desc)
        parcel.writeString(officialUrl)
        parcel.writeInt(level)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TorrentSource> {
        override fun createFromParcel(parcel: Parcel): TorrentSource {
            return TorrentSource(parcel)
        }

        override fun newArray(size: Int): Array<TorrentSource?> {
            return arrayOfNulls(size)
        }
    }
}