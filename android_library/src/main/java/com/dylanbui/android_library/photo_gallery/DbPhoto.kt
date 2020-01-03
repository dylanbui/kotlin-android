package com.dylanbui.android_library.photo_gallery

import android.app.Activity
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dylanbui.android_library.utils.readBoolean
import com.dylanbui.android_library.utils.writeBoolean

//@Parcelize
//data class Student(
//    var link: String? = null,
//    var isChoosed: Boolean = false,
//    var indexChoosed: Int = 0): Parcelable {
//
//    companion object CREATOR : Parcelable.Creator<Student> {
//        override fun createFromParcel(parcel: Parcel): Student {
//            return Student(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Student?> {
//            return arrayOfNulls(size)
//        }
//    }
//
//}

data class DbPhoto(var path: String? = null,
                   var link: String? = null,
                   var isChoosed: Boolean = false,
                   var indexChoosed: Int) : Parcelable {

    constructor(strPath: String?) : this(strPath, null, false, 0)
    constructor(strPath: String?, strLink: String?) : this(strPath, strLink, false, 0)

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        (parcel.readBoolean()),
        parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeString(link)
        parcel.writeBoolean(isChoosed)
        parcel.writeInt(indexChoosed)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DbPhoto> {
        override fun createFromParcel(parcel: Parcel): DbPhoto {
            return DbPhoto(parcel)
        }

        override fun newArray(size: Int): Array<DbPhoto?> {
            return arrayOfNulls(size)
        }
    }

    // --- Support function
    public fun loadToImageView(imageView: ImageView, activity: Any?) { // Any : Activity, Context
        // Or Activity
        (activity as? Activity)?.let {
            Glide.with(it).load(this.path)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().into(imageView)
        }
        // Or Context
        (activity as? Context)?.let {
            Glide.with(it).load(this.path)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().into(imageView)
        }
    }


}