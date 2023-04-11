package com.thewind.widget.actiondialog

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

/**
 * @author: read
 * @date: 2023/4/11 下午11:59
 * @description:
 */
@Keep
class NotifyData() : Parcelable {
    var type: Int = NotifyType.COMMON
    var cancelable: Boolean = false
    var title: String? = null
    var content: String? = null
    var leftButton: ButtonData? = null
    var rightButton: ButtonData? = null

    constructor(parcel: Parcel) : this() {
        type = parcel.readInt()
        title = parcel.readString()
        content = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
        parcel.writeString(title)
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotifyData> {
        override fun createFromParcel(parcel: Parcel): NotifyData {
            return NotifyData(parcel)
        }

        override fun newArray(size: Int): Array<NotifyData?> {
            return arrayOfNulls(size)
        }
    }
}

@Keep
class ButtonData() : Parcelable {
    var text: String? = null
    var actionUrl: String? = null
    var actionType: Int = ButtonActionType.NONE

    constructor(parcel: Parcel) : this() {
        text = parcel.readString()
        actionUrl = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(actionUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ButtonData> {
        override fun createFromParcel(parcel: Parcel): ButtonData {
            return ButtonData(parcel)
        }

        override fun newArray(size: Int): Array<ButtonData?> {
            return arrayOfNulls(size)
        }
    }
}

object NotifyType {
    const val COMMON = 0
    const val APP_UPDATE = 1
}

object ButtonActionType {
    const val NONE = 0
    const val INNER_H5 = 1
    const val INNER_PAGE = 2
    const val OUTER_JUMP = 3
}