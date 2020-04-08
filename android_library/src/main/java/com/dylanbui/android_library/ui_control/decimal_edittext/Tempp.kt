package com.dylanbui.android_library.ui_control.decimal_edittext

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class InputFromEndDecimalEditText : AppCompatEditText {
    // integer number of bits
    private val integerLength = 0
    // decimal places
    private val decimalLength = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(
        context: Context,
        attrs: AttributeSet?
    ) { //        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.InputFromEndDecimalEditText);
//        integerLength = a.getInteger(R.styleable.InputFromEndDecimalEditText_integerLength, 0);
//        decimalLength = a.getInteger(R.styleable.InputFromEndDecimalEditText_decimalLength, 0);
//        a.recycle();
//
//        / / Limit the input length of the EditText
//        setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerLength + decimalLength + 1)});
//        // The cursor is not visible (because the cursor is always at the end, so the user is not allowed to see the cursor)
//        setCursorVisible(false);
//        setTextChangeListener();
    }
}