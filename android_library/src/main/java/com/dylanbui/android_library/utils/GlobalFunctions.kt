package com.dylanbui.android_library.utils

import android.content.Context
import android.net.Uri
import android.os.Parcel
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

// Or : val myNonNullString = myString?.let { it } ?: return
/* Example:
    var myString : String? = null
    // myNonNullString is a String, not a String?
    val myNonNullString = myString.guard { return }
* */
inline fun <T> T.guard(func: T.() -> Unit): T {
    if (this == null) { func() }
    this.let { return it }
}

inline fun <reified T> fromJson(json: JsonElement): T {
    return Gson().fromJson(json, object: TypeToken<T>(){}.type)
}


/*

// Will print
val (first, second, third) = guardLet("Hello", 3, Thing("Hello")) { return }
println(first)
println(second)
println(third)

// Will return
val (first, second, third) = guardLet("Hello", null, Thing("Hello")) { return }
println(first)
println(second)
println(third)

// Will print
ifLet("Hello", "A", 9) { (first, second, third) ->
    println(first)
    println(second)
    println(third)
}

// Won't print
ifLet("Hello", 9, null) { (first, second, third) ->
    println(first)
    println(second)
    println(third)
}

* */

inline fun <T: Any> guardLet(vararg elements: T?, closure: () -> Nothing): List<T> {
    return if (elements.all { it != null }) {
        elements.filterNotNull()
    } else {
        closure()
    }
}

inline fun <T: Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it != null }) {
        closure(elements.filterNotNull())
    }
}


fun Uri.getPathString(context: Context): String {
    var path: String = ""

    context.contentResolver.query(
        this, arrayOf(MediaStore.Images.Media.DATA),
        null, null, null
    )?.apply {
        val columnIndex = getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        moveToFirst()
        path = getString(columnIndex)
        close()
    }

    return path
}

// Extension function to show toast message
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun dLog(str: String) {
    Log.d("TAG", str)
}


fun Parcel.writeBoolean(flag: Boolean) {
    when(flag) {
        true -> writeInt(1)
        false -> writeInt(0)
        else -> writeInt(0)
    }
}

fun Parcel.readBoolean(): Boolean {
    return when(readInt()) {
        1 -> true
        0 -> false
        else -> false
    }
}