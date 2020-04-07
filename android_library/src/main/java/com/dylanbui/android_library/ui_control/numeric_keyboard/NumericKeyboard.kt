package com.dylanbui.android_library.ui_control.numeric_keyboard

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import com.dylanbui.android_library.R
import com.dylanbui.android_library.ui_control.numeric_keyboard.editor.INumericEditor

class NumericKeyboard(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs), View.OnClickListener {
    private var numericButtons: Array<Button?> = arrayOf()
    private var numeric00: Button? = null
    private var numeric0: Button? = null
    private var numeric1: Button? = null
    private var numeric2: Button? = null
    private var numeric3: Button? = null
    private var numeric4: Button? = null
    private var numeric5: Button? = null
    private var numeric6: Button? = null
    private var numeric7: Button? = null
    private var numeric8: Button? = null
    private var numeric9: Button? = null
    private var backspace: ImageButton? = null
    private var done: ImageButton? = null
    private var unlimited: Button? = null

    private var enabledUnlimitedColor: Int = 0
    private var disabledUnlimitedColor: Int = 0

    private var vibrator: Vibrator? = null

    private var onKeyClickListener: OnKeyClickListener? = null
    private var numericEditor: INumericEditor? = null

    init {
        buildViews(context, attrs)
        if (!isInEditMode) {
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    private fun buildViews(context: Context, attrs: AttributeSet) {
        injectViews(context)
        setStyle(context, attrs)
        initUI()
    }

    private fun setStyle(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumericKeyboard)
        done?.let {
            val doneButtonSrc = typedArray.getDrawable(R.styleable.NumericKeyboard_nk_button_done_src)
            if (doneButtonSrc == null) {
                it.setImageResource(R.drawable.numeric_kb_sel_button_ok)
            } else it.setImageDrawable(doneButtonSrc)
            val doneBackground = typedArray.getDrawable(R.styleable.NumericKeyboard_nk_button_background)
            it.setBackgroundDrawable(doneBackground)
        }

        backspace?.let {
            val backspaceButtonSrc = typedArray.getDrawable(R.styleable.NumericKeyboard_nk_button_backspace_src)
            if (backspaceButtonSrc == null) {
                it.setImageResource(R.drawable.numeric_kb_vec_backspace)
                val topPadding: Int = it.resources.getDimension(R.dimen.backspaceTopPadding).toInt()
                it.setPadding(0, topPadding, 0, 0)
            } else it.setImageDrawable(backspaceButtonSrc)
            val backspaceBackground = typedArray.getDrawable(R.styleable.NumericKeyboard_nk_button_background)
            it.setBackgroundDrawable(backspaceBackground)
        }

        val fontFamily = typedArray.getString(R.styleable.NumericKeyboard_nk_button_fontFamily)
        val textColor = typedArray.getColor(R.styleable.NumericKeyboard_nk_button_textColor, Color.BLACK)
        val textSize = typedArray.getDimension(R.styleable.NumericKeyboard_nk_button_textSize, 12F)
        for (button in numericButtons) {
            button?.let {
                val background = typedArray.getDrawable(R.styleable.NumericKeyboard_nk_button_background)
                it.setTextColor(textColor)
                it.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                it.setBackgroundDrawable(background)
                it.typeface = Typeface.create(fontFamily, Typeface.NORMAL)
                it.gravity = Gravity.CENTER
            }
        }

        enabledUnlimitedColor = typedArray.getColor(R.styleable.NumericKeyboard_nk_button_enabledTextColor, Color.GREEN)
        disabledUnlimitedColor = textColor
        typedArray.recycle()
    }

    private fun injectViews(context: Context) {
        val keyboard = View.inflate(context, R.layout.view_numeric_keyboard, this)
        numeric00 = keyboard.findViewById(R.id.numeric_00) as Button?
        numeric0 = keyboard.findViewById(R.id.numeric_0) as Button?
        numeric1 = keyboard.findViewById(R.id.numeric_1) as Button?
        numeric2 = keyboard.findViewById(R.id.numeric_2) as Button?
        numeric3 = keyboard.findViewById(R.id.numeric_3) as Button?
        numeric4 = keyboard.findViewById(R.id.numeric_4) as Button?
        numeric5 = keyboard.findViewById(R.id.numeric_5) as Button?
        numeric6 = keyboard.findViewById(R.id.numeric_6) as Button?
        numeric7 = keyboard.findViewById(R.id.numeric_7) as Button?
        numeric8 = keyboard.findViewById(R.id.numeric_8) as Button?
        numeric9 = keyboard.findViewById(R.id.numeric_9) as Button?
        backspace = keyboard.findViewById(R.id.numeric_backspace) as ImageButton?
        done = keyboard.findViewById(R.id.numeric_done) as ImageButton?
        unlimited = keyboard.findViewById(R.id.numeric_unlimited) as Button?
        numericButtons = arrayOf(numeric00, numeric0, numeric1, numeric2, numeric3, numeric4,
                numeric5, numeric6, numeric7, numeric8, numeric9, unlimited)
    }

    private fun initUI() {
        for (button in numericButtons) {
            button?.setOnClickListener(this)
        }
        backspace?.setOnClickListener(this)
        done?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        onNumericClick(v)
    }

    private fun onNumericClick(view: View) {
        when (view.id) {
            R.id.numeric_0 -> deliverClick(Key.NUMBER_0, "0")
            R.id.numeric_1 -> deliverClick(Key.NUMBER_1, "1")
            R.id.numeric_2 -> deliverClick(Key.NUMBER_2, "2")
            R.id.numeric_3 -> deliverClick(Key.NUMBER_3, "3")
            R.id.numeric_4 -> deliverClick(Key.NUMBER_4, "4")
            R.id.numeric_5 -> deliverClick(Key.NUMBER_5, "5")
            R.id.numeric_6 -> deliverClick(Key.NUMBER_6, "6")
            R.id.numeric_7 -> deliverClick(Key.NUMBER_7, "7")
            R.id.numeric_8 -> deliverClick(Key.NUMBER_8, "8")
            R.id.numeric_9 -> deliverClick(Key.NUMBER_9, "9")
            R.id.numeric_00 -> deliverClick(Key.NUMBER_00, "00")
            R.id.numeric_backspace -> deliverClick(Key.BACKSPACE, null)
            R.id.numeric_unlimited -> deliverClick(Key.UNLIMITED, null)
            R.id.numeric_done -> deliverClick(Key.DONE, null)
        }
        vibrate()
    }

    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(200)
        }

//        try {
//            vibrator?.vibrate(20)
//        } catch(e: SecurityException) {
//            Log.e(e.javaClass.name, e.message, e)
//        }
    }

    fun setOnKeyClickListener(onKeyClickListener: OnKeyClickListener?) {
        this.onKeyClickListener = onKeyClickListener
    }

    private fun deliverClick(key: Key, ch: String?) {
        Log.i("Button clicked", key.toString())
        numericEditor?.onKeyDown(key, ch)
        setUnlimitedOn(numericEditor?.isUnlimited() ?: false)

        onKeyClickListener?.onKeyDown(key, ch)
    }

    private fun setMode(mode: Mode) {
        when (mode) {
            Mode.LIMITED -> unlimited?.visibility = View.INVISIBLE
            Mode.UNLIMITED -> unlimited?.visibility = View.VISIBLE
        }
    }

    fun setNumericEditor(numericEditor: INumericEditor?) {
        this.numericEditor = numericEditor
        if (numericEditor != null) {
            setMode(numericEditor.getMode())
            setUnlimitedOn(numericEditor.isUnlimited())
        }
    }

    private fun setUnlimitedOn(on: Boolean) {
        unlimited?.setTextColor(if (on) enabledUnlimitedColor else disabledUnlimitedColor)
    }

    interface OnKeyClickListener {
        fun onKeyDown(key: Key, ch: CharSequence?)
    }
}
