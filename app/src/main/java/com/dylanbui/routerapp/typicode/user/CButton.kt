package com.dylanbui.routerapp.typicode.user

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.dylanbui.android_library.ui_control.button.FitButton
import com.dylanbui.routerapp.R

class CButton : FitButton {

    constructor(context: Context) : super(context) {
        defaultSetup(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        defaultSetup(context)
    }

    private fun defaultSetup(context: Context) {

        // setTextFont()
        setTextSize(20f)
            setIconMarginStart(16f)
            setIconMarginEnd(12f)
            setTextColor(Color.parseColor("#F5F5F5"))
            setIconColor(Color.parseColor("#FFFFFF"))
            setDividerColor(Color.parseColor("#BCAAA4"))
            setBorderColor(Color.parseColor("#FFF59D"))
            setButtonColor(Color.parseColor("#FF7043"))
            setBorderWidth(2f)
            setRippleEnable(true)
            setRippleColor(resources.getColor(R.color.colorAccent, null))


    }

}