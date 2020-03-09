package com.dylanbui.routerapp.external_activity.models

import android.os.Parcel;
import android.os.Parcelable;
import com.dylanbui.android_library.utils.readBoolean
import com.dylanbui.android_library.utils.writeBoolean


class Artist(var name: String?, var isFavorite: Boolean = true) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        (parcel.readBoolean())) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeBoolean(isFavorite)
    }


//    protected constructor(in: Parcel) {
//        name = `in`.readString()
//    }
//
//    fun getName(): String? {
//        return name
//    }
//
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Artist) return false
        val artist = o
        if (isFavorite != artist.isFavorite) return false
        return if (name != null) name == artist.name else artist.name == null
    }
//
    override fun hashCode(): Int {
        var result = if (name != null) name.hashCode() else 0
        result = 31 * result + if (isFavorite) 1 else 0
        return result
    }

//    override fun writeToParcel(dest: Parcel, flags: Int) {
//        dest.writeString(name)
//    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Artist> {
        override fun createFromParcel(parcel: Parcel): Artist {
            return Artist(parcel)
        }

        override fun newArray(size: Int): Array<Artist?> {
            return arrayOfNulls(size)
        }
    }
}

