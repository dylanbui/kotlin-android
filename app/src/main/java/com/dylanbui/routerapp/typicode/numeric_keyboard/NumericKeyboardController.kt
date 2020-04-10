package com.dylanbui.routerapp.typicode.numeric_keyboard

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.EditText
import com.dylanbui.android_library.ui_control.numeric_keyboard.NumericKeyboard
import com.dylanbui.android_library.ui_control.numeric_keyboard.editor.NumericEditText
import com.dylanbui.android_library.ui_control.numeric_keyboard.setup.IKeyboardManager
import com.dylanbui.android_library.ui_control.numeric_keyboard.setup.KeyboardManager
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat


class NumericKeyboardController :
    BaseMvpController<NumericKeyboardViewAction, NumericKeyboardPresenter>(),
    NumericKeyboardViewAction {

    private lateinit var content: ViewGroup
    private lateinit var space: View
    private lateinit var keyboard: NumericKeyboard

    private lateinit var simpleEditText1: EditText
    private lateinit var simpleEditText2: EditText
//    private var simpleEditText3: EditText? = null
//    private var simpleEditText4: EditText? = null
    private lateinit var numericEditText1: NumericEditText
    private lateinit var numericEditText2: NumericEditText
//    private var numericEditText3: NumericEditText? = null
//    private var numericEditText4: NumericEditText? = null

    private lateinit var keyboardSetup: IKeyboardManager


    override fun setTitle(): String? = "NumericKeyboard"

    override fun createPresenter(): NumericKeyboardPresenter = NumericKeyboardPresenter()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_numeric_keyboard, container, false)
    }

    override fun onViewBound(view: View) {
        injectViews(view)
        settingKeyboard(content, space, keyboard)
    }

    override fun onPreAttach() {
        // -- At here presenter is existed --
        // -- Get action in row => presenter --
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
    }


    private fun settingKeyboard(content: ViewGroup, space: View, keyboard: NumericKeyboard) {
        keyboardSetup = KeyboardManager(content, space, keyboard) as IKeyboardManager
        keyboardSetup.setupSimpleEditTextViews(simpleEditText1, simpleEditText2)
        keyboardSetup.setupNumericEditTextViews(numericEditText1, numericEditText2)
    }



    private fun injectViews(view: View) {

        numericEditText1 = view.findViewById(R.id.numeric_edit_text_1) as NumericEditText
        numericEditText2 = view.findViewById(R.id.numeric_edit_text_2) as NumericEditText

        simpleEditText1 = view.findViewById(R.id.simple_edit_text_1) as EditText
        simpleEditText2 = view.findViewById(R.id.simple_edit_text_2) as EditText

        content = view.findViewById(R.id.contentScrollView) as ViewGroup
        space = view.findViewById(R.id.space) as View
        keyboard = view.findViewById(R.id.keyboardOld) as NumericKeyboard



    }

//    override fun handleBack(): Boolean {
//        if (!keyboardSetup.onBackPressed()) {
//            return super.handleBack()
//        }
//        return false
//    }

//    override fun handleBack(): Boolean {
//        if (!keyboardSetup!!.onBackPressed()) return super.handleBack()
//        // return super.handleBack()
//    }

}

