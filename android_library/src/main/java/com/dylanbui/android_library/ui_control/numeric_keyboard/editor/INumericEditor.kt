package com.dylanbui.android_library.ui_control.numeric_keyboard.editor

import com.dylanbui.android_library.ui_control.numeric_keyboard.Key
import com.dylanbui.android_library.ui_control.numeric_keyboard.Mode

interface INumericEditor {
    fun getMode(): Mode
    fun isUnlimited(): Boolean
    fun onKeyDown(key: Key, ch: CharSequence?)
}
