package com.dylanbui.android_library.ui_control.decimal_edittext

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.dylanbui.android_library.R
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

/* DucBui 09/04/2020 make on:

https://github.com/PhanVanLinh/AndroidCurrencyEditText
https://github.com/aldoKelvianto/AutoFormatEditText

* */

class DbAutoFormatEditText : AppCompatEditText  {

    companion object {
        private const val MAX_LENGTH = 19 // '123,456,789,123,456' => length 19
        private const val MAX_DECIMAL_DIGIT = 2

        private const val DECIMAL_SEPARATOR: String = "."
        private const val GROUP_SEPARATOR: String = ","
        private const val DECIMAL_FORMAT = "#${GROUP_SEPARATOR}###${DECIMAL_SEPARATOR}###" // "#,###.###" 3 so le
    }

    private var isDecimal = false
    private lateinit var currencyTextWatcher: CurrencyTextWatcher

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        this.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        this.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(MAX_LENGTH))

        obtainAttributes(attrs)

        currencyTextWatcher = CurrencyTextWatcher(this, isDecimal)
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArrays = context.obtainStyledAttributes(attrs, R.styleable.AutoFormatEditText)
            try {
                isDecimal = typedArrays.getBoolean(R.styleable.AutoFormatEditText_isDecimal, false)
            } finally {
                typedArrays.recycle()
            }
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            addTextChangedListener(currencyTextWatcher)
        } else {
            removeTextChangedListener(currencyTextWatcher)
        }
    }

    fun setValue(number: String) {
        setValue(number.toDouble())
    }

    fun setValue(number: Double) {
//        var strValue = number.toString()
//        Log.e("TAG", "str = " + strValue)
        val number = number.toString().replace(".", DECIMAL_SEPARATOR)
        val str = currencyTextWatcher.formatNumber(number)
        this.setText(str)
    }

    fun getValue(): Double {
        val str = text.toString()
        if (str.isEmpty()) {
            return 0.0
        }

        // -- Remove prefix, and GROUP_SEPARATOR (",") --
        val number = str.replace("[${GROUP_SEPARATOR}]".toRegex(), "")
        // -- Replace right DECIMAL_SEPARATOR is "." --
        val rightNumber = number.replace(DECIMAL_SEPARATOR, ".")
        return rightNumber.toDouble()
    }

    private class CurrencyTextWatcher(private val editText: EditText, private val isDecimal: Boolean) : TextWatcher {

        companion object {
            private const val MAX_LENGTH = 19
            private const val ACTION_DELETE = 0
            private const val ACTION_INSERT = 1
        }

        private var prevCommaAmount = 0
        private var isFormatting = false

        private var integerFormatter: DecimalFormat = DecimalFormat("#,###.###", DecimalFormatSymbols(Locale.US))
        private var decimalSymbols: DecimalFormatSymbols = DecimalFormatSymbols(Locale.US)

        init {
            decimalSymbols.decimalSeparator = DECIMAL_SEPARATOR[0]
            decimalSymbols.groupingSeparator = GROUP_SEPARATOR[0]
            integerFormatter = DecimalFormat("#,###", decimalSymbols)
        }

        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (isFormatting) {
                return
            }

            if (s != null && s.isNotEmpty()) {
                isFormatting = true
                formatInput(s.toString(), start, count)
            }

            isFormatting = false
        }


        /**
         * This is the main method which makes use of addNum method.
         *
         * @param input  an input text.
         * @param start  start of a text.
         * @param action delete or insert(0 == Delete and 1 == Insert) text.
         * @return Nothing.
         */
        @SuppressLint("SetTextI18n")
        private fun formatInput(input: String, start: Int, action: Int) {
            var sbResult = "" //StringBuilder()
            var result: String
            var newStart = start
            try { // Extract value without its comma
                var digitAndDotText = input.replace(",", "")
                var commaAmount = 0
                // if user press . turn it into 0.
                if (input.startsWith(".") && input.length == 1) {
                    if (isDecimal) {
                        editText.setText("0.")
                        editText.setSelection(editText.text.toString().length)
                    } else {
                        editText.setText("")
                    }

                    return
                }
                // if user press . when number already exist turns it into comma
                if (input.startsWith(".") && input.length > 1) {
                    // Kiem tra them trung hop neu user bam 0 hay dau . o dau
                    // Phia xu ly remove no ra neu isDecimal == false
                    if (!isDecimal) {
                        // Remove fist character insert (is .)
                        editText.setText(input.substring(1))
                        editText.setSelection(0)
                        return
                    }

                    // Case : Use input '1234' after input . before '1' => '.1234'
                    // Neu da co . truoc do, he thong se khong xu ly
                    // co le he thong da xu ly thay khi phat hien neu co . roi
                    val st = StringTokenizer(input)
                    val afterDot = st.nextToken(".")
                    val strOnlyDigits = afterDot.replace("\\D+".toRegex(), "")
                    // editText.setText("0.$strOnlyDigits") //DbAutoFormatUtil.extractDigits(afterDot))
                    digitAndDotText = "0.$strOnlyDigits"
                }

//                if (digitAndDotText.contains(".")) { // escape sequence for .
//                    val wholeText = digitAndDotText.split("\\.").toTypedArray()
//                    if (wholeText.isEmpty()) {
//                        return
//                    }
//
//                    // in 150,000.45 non decimal is 150,000 and decimal is 45
//                    val nonDecimal = wholeText[0]
//                    if (nonDecimal.isEmpty()) {
//                        return
//                    }
//
//                    // only format the non-decimal value
//                    result = DbAutoFormatUtil.formatToStringWithoutDecimal(nonDecimal)
//                    sbResult.append(result).append(".")
//
//                    if (wholeText.size > 1) {
//                        sbResult.append(wholeText[1].subSequence(0, 2))
//                    }
//
//                } else {
//                    result = DbAutoFormatUtil.formatWithDecimal(digitAndDotText)
//                    sbResult.append(result)
//                }

                sbResult = formatNumber(digitAndDotText)
                val wholeText = sbResult.split(DECIMAL_SEPARATOR).toTypedArray()
                // result = the non-decimal value
                result = if (wholeText.isEmpty()) sbResult else wholeText[0]

                newStart += if (action == ACTION_DELETE) 0 else 1

                // calculate comma amount in edit text
                commaAmount += getCommaOccurrence(result)
                // flag to mark whether new comma is added / removed
                if (commaAmount >= 1 && prevCommaAmount != commaAmount) {
                    newStart += if (action == ACTION_DELETE) -1 else 1
                    prevCommaAmount = commaAmount
                }
                // case when deleting without comma
                if (commaAmount == 0 && action == ACTION_DELETE && prevCommaAmount != commaAmount) {
                    newStart -= 1
                    prevCommaAmount = commaAmount
                }
                // case when deleting without dots
                if (action == ACTION_DELETE && !sbResult.toString().contains(".") && prevCommaAmount != commaAmount) {
                    newStart = start
                    prevCommaAmount = commaAmount
                }
                // -- end

                val resultLength = sbResult.toString().length

                // case when dots become comma and vice versa.
                if (action == ACTION_INSERT && commaAmount == 0 && sbResult.toString().contains(".")) {
                    // newStart = start;
                }

                editText.setText(sbResult.toString())

                // ensure newStart is within result length
                if (newStart > resultLength) {
                    newStart = resultLength
                } else if (newStart < 0) {
                    newStart = 0
                }
                editText.setSelection(newStart)

            } catch (e: NumberFormatException) {
                e.printStackTrace()
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }
        }


        ///////////------------------------

        internal fun formatNumber(number: String): String {
            if (isDecimal) {
                return if (number.contains(DECIMAL_SEPARATOR)) formatDecimal(number) else formatInteger(number)
            }
            return formatInteger(number)
        }

        private fun formatInteger(str: String): String {
            val parsed = BigDecimal(str)
            return integerFormatter.format(parsed)
        }

        private fun formatDecimal(str: String): String {
            if (str == DECIMAL_SEPARATOR) {
                return DECIMAL_SEPARATOR
            }
            val parsed = BigDecimal(str)

            // do format cho nay lam mat dau cham
            // example pattern VND #,###.00
            // "#,###." + getDecimalPattern(str)
            var decimalFormatTemp = "#${GROUP_SEPARATOR}###${DECIMAL_SEPARATOR}" + getDecimalPattern(str)

            if (parsed < BigDecimal(1.0)) { // is 0.X
                decimalFormatTemp = "0${DECIMAL_SEPARATOR}" + getDecimalPattern(str)
            }

//            var decimalFormatTemp = "#${GROUP_SEPARATOR}###"
//            val decimalPattern = getDecimalPattern(str)
//            if (decimalPattern.isNotEmpty()) {
//                decimalFormatTemp = "$decimalFormatTemp${DECIMAL_SEPARATOR}" + getDecimalPattern(str)
//            }

            val formatter = DecimalFormat(decimalFormatTemp, decimalSymbols)
//            val formatter = DecimalFormat(
//                "#,###." + getDecimalPattern(str),
//                DecimalFormatSymbols(Locale.US)
//            )
            formatter.roundingMode = RoundingMode.DOWN
            return formatter.format(parsed)
        }

        /**
         * It will return suitable pattern for format decimal
         * For example: 10.2 -> return 0 | 10.23 -> return 00, | 10.235 -> return 000
         */
        private fun getDecimalPattern(str: String): String {
            val decimalCount = str.length - str.indexOf(DECIMAL_SEPARATOR) - 1
            val decimalPattern = StringBuilder()
            var i = 0
            while (i < decimalCount && i < MAX_DECIMAL_DIGIT) {
                decimalPattern.append("0")
                i++
            }
            return decimalPattern.toString()
        }

        // Get num ',' in input string
        fun getCommaOccurrence(input: String): Int {
            return getCharOccurrence(input, ',')
        }

        private fun getCharOccurrence(input: String, c: Char): Int {
            var occurrence = 0
            val chars = input.toCharArray()
            for (thisChar in chars) {
                if (thisChar == c) {
                    occurrence++
                }
            }
            return occurrence
        }

    }

}


//private object DbAutoFormatUtil {
//
//    private const val FORMAT_NO_DECIMAL = "###,###"
//    private const val FORMAT_WITH_DECIMAL = "###,###.###"
//    private fun getCharOccurrence(input: String, c: Char): Int {
//        var occurrence = 0
//        val chars = input.toCharArray()
//        for (thisChar in chars) {
//            if (thisChar == c) {
//                occurrence++
//            }
//        }
//        return occurrence
//    }
//
//    @JvmStatic
//    fun getCommaOccurrence(input: String): Int {
//        return getCharOccurrence(input, ',')
//    }
//
//    @JvmStatic
//    fun extractDigits(input: String): String {
//        return input.replace("\\D+".toRegex(), "")
//    }
//
//    private fun formatToStringWithoutDecimal(value: Double): String {
//        val formatter: NumberFormat =
//            DecimalFormat(FORMAT_NO_DECIMAL)
//        return formatter.format(value)
//    }
//
//    @JvmStatic
//    fun formatToStringWithoutDecimal(value: String): String {
//        return formatToStringWithoutDecimal(value.toDouble())
//    }
//
//    fun formatWithDecimal(price: String): String {
//        return formatWithDecimal(price.toDouble())
//    }
//
//    private fun formatWithDecimal(price: Double): String {
//        val formatter: NumberFormat = DecimalFormat(FORMAT_WITH_DECIMAL)
//        return formatter.format(price)
//    }
//}