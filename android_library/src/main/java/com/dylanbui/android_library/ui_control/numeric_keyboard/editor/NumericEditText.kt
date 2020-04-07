package com.dylanbui.android_library.ui_control.numeric_keyboard.editor

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.EditText
import com.dylanbui.android_library.R
import com.dylanbui.android_library.ui_control.numeric_keyboard.Key
import com.dylanbui.android_library.ui_control.numeric_keyboard.Mode


import java.text.DecimalFormat


class NumericEditText(context: Context, attrs: AttributeSet) : EditText(context, attrs), INumericEditor {
    private var unlimited: Boolean = false
    private var value = 0.0
    var onDoneClickListener: OnDoneClickListener? = null

    init {
        inputType = InputType.TYPE_NULL
        hint = decimalFormat.format(value)
    }

    override fun getMode(): Mode {
        return Mode.UNLIMITED
    }

    override fun isUnlimited(): Boolean {
        return unlimited
    }

    override fun onKeyDown(key: Key, ch: CharSequence?) {
        when (key) {
            Key.NUMBER_0, Key.NUMBER_1, Key.NUMBER_2, Key.NUMBER_3, Key.NUMBER_4, Key.NUMBER_5,
            Key.NUMBER_6, Key.NUMBER_7, Key.NUMBER_8, Key.NUMBER_9 -> {
                unlimited = false
                value = value * 10 + java.lang.Double.valueOf(ch.toString())!! / 100
            }
            Key.NUMBER_00 -> value *= 100
            Key.BACKSPACE -> if (!unlimited)
                value = (value * 100 - value * 100 % 10) / 1000
            Key.DONE -> onDoneClickListener?.onDoneClicked()
            Key.UNLIMITED -> unlimited = !unlimited
        }
        invalidateValue()
    }

    private fun invalidateValue() {
        if (!unlimited)
            setText(decimalFormat.format(value))
        else
            setText(R.string.keyboard_caption_unlimited)
    }

    fun getValue(): Double {
        return java.lang.Double.valueOf(decimalFormat.format(value))!!
    }

    companion object {
        private val decimalFormat: DecimalFormat

        init {
            decimalFormat = DecimalFormat()
            decimalFormat.isDecimalSeparatorAlwaysShown = true
            decimalFormat.minimumFractionDigits = 2
            decimalFormat.maximumFractionDigits = 2
        }
    }
}
