package com.dylanbui.routerapp.external_activity.single_check

import android.os.Parcel
import android.os.Parcelable
import com.dylanbui.android_ui_custom.expandable_check_recyclerview.models.SingleCheckExpandableGroup

class SingleCheckGenre: SingleCheckExpandableGroup {

    var iconResId: Int

    constructor(parcel: Parcel): super(parcel) {
        iconResId = parcel.readInt()
    }

    constructor(title: String?, items: List<*>?, mIconResId: Int): super(title, items) {
        iconResId = mIconResId
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeInt(iconResId)
    }

    companion object CREATOR : Parcelable.Creator<SingleCheckGenre> {
        override fun createFromParcel(parcel: Parcel): SingleCheckGenre {
            return SingleCheckGenre(parcel)
        }

        override fun newArray(size: Int): Array<SingleCheckGenre?> {
            return arrayOfNulls(size)
        }
    }


}
