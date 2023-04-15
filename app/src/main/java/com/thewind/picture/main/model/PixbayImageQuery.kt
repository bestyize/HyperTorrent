package com.thewind.picture.main.model

import android.os.Parcel
import android.os.Parcelable
import com.thewind.util.urlEncode
import java.util.Base64

/**
 * @author: read
 * @date: 2023/4/15 下午8:29
 * @description:
 * https://pixabay.com/api/docs/
 */
class PixbayImageQuery() : Parcelable {
    var q: String? = null
    var lang: String = "zh"
    var page: Int = 1
    var num: Int = 10
    var imageType: String = "photo"

    constructor(parcel: Parcel) : this() {
        q = parcel.readString()
        lang = parcel.readString()?:"zh"
        page = parcel.readInt()
        num = parcel.readInt()
        imageType = parcel.readString()?:ImageType.PHOTO.type
    }
   //https://pixabay.com/api/?key=35359169-d0387b5efe4184e8a51381124&q=%E5%A3%81%E7%BA%B8&image_type=all&page=2&per_page=20&lang=zh
    fun toUrlQueryWitnBase64(): String {
       val query = q?.replace(Regex("\\s+"), "+").urlEncode()
       val queryStr = "q=$query&lang=$lang&page=$page&per_page=$num&image_type=$imageType"
       return Base64.getEncoder().encodeToString(queryStr.toByteArray(Charsets.UTF_8))
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(q)
        parcel.writeString(lang)
        parcel.writeInt(page)
        parcel.writeInt(num)
        parcel.writeString(imageType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PixbayImageQuery> {
        override fun createFromParcel(parcel: Parcel): PixbayImageQuery {
            return PixbayImageQuery(parcel)
        }

        override fun newArray(size: Int): Array<PixbayImageQuery?> {
            return arrayOfNulls(size)
        }
    }


}

enum class ImageType(val type: String) {
    // "all", "photo", "illustration", "vector"
    ALL("all"),
    PHOTO("photo"),
    ILLUSTRATION("illustration"),
    VECTOR("vector")
}