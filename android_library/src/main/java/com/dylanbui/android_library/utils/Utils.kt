package com.dylanbui.android_library.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Environment
import android.text.format.DateFormat
import android.util.DisplayMetrics
import android.util.Patterns
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.dylanbui.android_library.DictionaryType
import com.dylanbui.android_library.R
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import java.net.URLEncoder
import java.util.*
import java.util.regex.Pattern
import kotlin.math.roundToInt

// @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
// @Suppress
@SuppressLint("all")
object Utils {

    fun getCompressFolderPath(context: Context): String {
        return (Environment.getExternalStorageDirectory().toString()
                + "/Android/data/"
                + context.applicationContext.packageName
                + "/Files/Compressed")
    }

    fun View.OnClickListener.listenToViews(vararg views: View) =
        views.forEach { it.setOnClickListener(this) }

    fun pxToDp(context: Context, px: Int): Int {
        val displayMetrics = context.resources
            .displayMetrics
        return (px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    fun isPhoneNumber(phone: String): Boolean {
        return Pattern.compile("(\\+84|0)\\d{9,10}").matcher(phone).matches()
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun delayFunc(delayTimeInMillisecond: Long, complete: ()->Unit) {
        GlobalScope.launch { // launch a new coroutine
            // Log.d("TAG", "-- jobUpdate --- ${Thread.currentThread()} has run.")
            delay(delayTimeInMillisecond)
            withContext(Dispatchers.Main) {
                complete()
            }
        }
    }

    fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }

    fun pxToDp(px: Int): Float {
        return (px / Resources.getSystem().displayMetrics.density)
    }

    fun convertDp2Px(dp: Int, context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
    }

    fun convertSpToPixels(activity: Activity, sp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            activity.resources.displayMetrics
        ).toInt()
    }

    fun capitalize(str: String?): String {
        val myNonNullString = str?.let { it } ?: return ""

        val first = myNonNullString[0]
        return if (Character.isUpperCase(first)) {
            myNonNullString
        } else {
            Character.toUpperCase(first) + myNonNullString.substring(1)
        }
    }

    fun roundStar(star: Float): Float {
        val iPart = star.toInt()
        val fPart = (star - iPart).toFloat()
        return if (fPart > 0 && fPart <= 0.5)
            (iPart + 0.5).toFloat()
        else if (fPart > 0.5 && fPart < iPart + 1)
            (iPart + 1).toFloat()
        else
            iPart.toFloat()
    }

    fun convertTimestampDateString(timestamp: Long): String {
        var date = ""

        val cal = Calendar.getInstance(Locale.ENGLISH)
        //cal.setTimeInMillis(timestamp*1000);
        cal.timeInMillis = timestamp
        date = DateFormat.format("dd/MM/yyyy", cal).toString()

        return date
    }

    fun formatTime(numSecond: Int, context: Context): String {
        var result = ""
        // nếu nhỏ hơn 1 giờ thì tính là 1 giờ
        if (numSecond < 3600)
            result = "1 " + context.resources.getString(R.string.hour)
        else if (numSecond < 86400)
            result =
                (numSecond / 3600).toString() + " " + context.resources.getString(R.string.hour)
        else if (numSecond < 5184000) { // dưới 2 tháng: 2*30*86400
            result =
                (numSecond / 86400).toString() + " " + context.resources.getString(R.string.day)
        } else if (numSecond < 63072000) {// dưới 2 năm: 2*365*86400
            result =
                (numSecond / 2592000).toString() + " " + context.resources.getString(R.string.month)
        } else {
            // nếu thời gian từ 1 ngày trở lên thì tính theo ngày
            result = (numSecond / 31536000).toString() + " " + context.resources.getString(R.string.year)
        }
        // nếu nhỏ hơn 1 ngày thì tính là giờ
        // còn lại tính theo năm
        return result
    }

    fun formatTimeDate(timestamp: Long): String {
        var date = ""

        val cal = Calendar.getInstance(Locale.ENGLISH)
        //cal.setTimeInMillis(timestamp*1000);
        cal.timeInMillis = timestamp
        date = DateFormat.format("HH:mm - dd/MM/yyyy", cal).toString()

        return date
    }

    fun formatDistance(m: Float): String {
        var result = ""
        if (m < 1000) {
            result = m.toInt().toString() + "m"
        } else {
            result = String.format("%.1f", m / 1000) + "km"
        }
        return result
    }

    fun makeProgressDialog(context: Context, message:String): AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = message
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20.toFloat()
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)

        val dialog = builder.create()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }

    fun convertToJsonObject(dict: DictionaryType): JsonObject {
        var jsonObject = JsonObject()

        dict.forEach {
            // New ways
            when (it.value) {
                null -> jsonObject.add(it.key, JsonNull.INSTANCE)
                is Boolean -> jsonObject.addProperty(it.key, it.value as Boolean)
                is Number -> jsonObject.addProperty(it.key, it.value as Number)
                else -> jsonObject.addProperty(it.key, it.value.toString())
            }
        }
        return jsonObject
    }

    fun convertToUrlEncode(map: DictionaryType): String {
        var sb = StringBuilder()
        map.entries.forEach {
            if (it.value == null) {
                sb.append(it.key).append('=').append('&')
            } else {
                sb.append(it.key)
                    .append('=')
                    .append(URLEncoder.encode(it.value.toString(), "UTF_8"))
                    .append('&')
            }
        }
        sb.delete(sb.length - 1, sb.length)
        return sb.toString()
    }
}