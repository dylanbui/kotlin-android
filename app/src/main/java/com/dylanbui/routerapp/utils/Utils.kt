package com.dylanbui.routerapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.os.Build
import android.text.SpannableString
import android.text.TextUtils
import android.text.format.DateFormat
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.utils.Constants.Companion.TAG
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.math.roundToInt

// @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
// @Suppress
@SuppressLint("all")
object Utils {

    @SuppressLint("MissingPermission")
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        //should check null because in airplane mode it will be null
        return netInfo != null && netInfo.isConnected
    }

    fun View.OnClickListener.listenToViews(vararg views: View) =
        views.forEach { it.setOnClickListener(this) }

    fun hideKeyboard(activity: Activity) {
        val inputManager = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus:
        val currentFocusedView = activity.currentFocus
        if (currentFocusedView != null) {
            inputManager!!.hideSoftInputFromWindow(
                currentFocusedView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    fun hideSoftKeyboard(activity: Activity) {
        try {
            val inputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken, 0
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun requestFocus(activity: Activity, editText: EditText) {
        editText.requestFocus()
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun disableCopyPasteEditText(editText: EditText) {
        editText.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return false
            }
            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }
            override fun onDestroyActionMode(p0: ActionMode?) {
            }
        }
    }

    fun underLineTextView(tv: TextView) {
        if (TextUtils.isEmpty(tv.text)) return
        val content = SpannableString(tv.text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        tv.text = content
    }

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

    fun delayExecute(delayTimeInMillisecond: Int): Observable<Long> {
        return Observable
            .create(ObservableOnSubscribe<Long> { e ->
                e.onNext(0L)
                e.onComplete()
            })
            .delay(delayTimeInMillisecond.toLong(), TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getVersionApp(context: Context): String {
        var result = ""
        try {
            val manager = context.packageManager
            var info: PackageInfo? = null
            info = manager.getPackageInfo(
                context.packageName, 0
            )
            result = info!!.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return result
    }

    fun getWidthScreen(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun getHeightScreen(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
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

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else {
            capitalize(manufacturer) + " " + model
        }
    }

    fun getVersionName(): String = Build.VERSION.RELEASE

    fun getDeviceToken(): String? {
        var token: String? = ""
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                token = task.result?.token
            })
        return token
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

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        var matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    fun getExactllyImageRotate(photoPath: String, bitmap: Bitmap): Bitmap? {
        var ei = ExifInterface(photoPath)
        var orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        var rotatedBitmap: Bitmap? = null
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap = rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap = rotateImage(bitmap, 270f)
            else -> rotatedBitmap = bitmap
        }
        return rotatedBitmap
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
}