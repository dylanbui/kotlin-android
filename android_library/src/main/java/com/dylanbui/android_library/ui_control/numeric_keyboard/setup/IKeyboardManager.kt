package com.dylanbui.android_library.ui_control.numeric_keyboard.setup

import android.widget.EditText
import com.dylanbui.android_library.ui_control.numeric_keyboard.editor.NumericEditText


interface IKeyboardManager {
    fun setupSimpleEditTextViews(vararg views: EditText)

    fun setupNumericEditTextViews(vararg views: NumericEditText)

    fun onBackPressed(): Boolean

    interface Callback {
        fun onCompleted()
    }
}