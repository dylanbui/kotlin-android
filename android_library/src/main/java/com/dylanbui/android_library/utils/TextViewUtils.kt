package com.dylanbui.android_library.utils

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.widget.TextView

@SuppressLint("all")
object TextViewUtils {

    fun underLineTextView(tv: TextView) {
        if (TextUtils.isEmpty(tv.text)) return
        val content = SpannableString(tv.text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        tv.text = content
    }

}