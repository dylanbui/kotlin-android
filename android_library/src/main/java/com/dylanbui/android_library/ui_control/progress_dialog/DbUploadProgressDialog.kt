package com.dylanbui.android_library.ui_control.progress_dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.dylanbui.android_library.R
import java.text.NumberFormat


class DbUploadProgressDialog(context: AppCompatActivity) : AlertDialog(context) {

    private var mMaxFileVal: Int = 0
    private var mCurrentFileVal: Int = 0

    private var mProgress: ProgressBar? = null
    private var mMessageView: TextView? = null

    private var mProgressNumber: TextView? = null
    private var mProgressNumberFormat: String? = null
    private var mProgressPercent: TextView? = null
    private var mProgressPercentFormat: NumberFormat? = null

    private var mProgressVal: Int = 0
    private var mProgressDrawable: Drawable? = null
    private var mMessage: String = ""

    private var mViewUpdateHandler: Handler? = null

    private var mProgressDialogView: View? = null
    private lateinit var mView: View

    init {

        initFormats()

        if (mProgressVal > 0) setProgress(mProgressVal, mCurrentFileVal)
        if (mProgressDrawable != null) setProgressDrawable(mProgressDrawable!!)
        setMessage(mMessage)
    }

    private fun initFormats() {
        mProgressNumberFormat = "%1d/%2d"
        mProgressPercentFormat = NumberFormat.getPercentInstance()
        mProgressPercentFormat?.maximumFractionDigits = 0
    }

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        /* Initialize spinner layout as the default layout */
        horizontalLayout()

        onProgressChanged()

        setCancelable(false)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        super.onCreate(savedInstanceState)
    }

    private fun horizontalLayout() {

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.progress_dialog_horizontal, null, false)
        // Defautl black - 60%
        // view.background = ColorDrawable(Color.parseColor("#99000000"))
        /* Use a separate handler to update the text views as they
         * must be updated on the same thread that created them.
         */
        mViewUpdateHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                /* Update the number min max file */
                if (mProgressNumberFormat != null) {
                    val format = mProgressNumberFormat
                    mProgressNumber?.text = format?.let { String.format(it, mCurrentFileVal, mMaxFileVal) }
                } else {
                    mProgressNumber?.text = ""
                }
                /* Update the percent */
                if (mProgressPercentFormat != null) {
                    val progress = mProgress?.progress
                    val percent = (progress?.toDouble() ?: 0.0) / (mProgress?.max?.toDouble() ?: 100.0)
                    val tmp = SpannableString(mProgressPercentFormat?.format(percent))
                    tmp.setSpan(StyleSpan(android.graphics.Typeface.BOLD),
                        0, tmp.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    mProgressPercent?.text = tmp
                } else {
                    mProgressPercent?.text = ""
                }
            }
        }

        mProgressDialogView = view.findViewById(R.id.progressDialogView)

        mProgress = view.findViewById<View>(R.id.progress) as ProgressBar
        mProgressNumber = view.findViewById<View>(R.id.progress_number) as TextView
        mProgressPercent = view.findViewById<View>(R.id.progress_percent) as TextView
        mMessageView = view.findViewById<View>(R.id.message) as TextView

        mProgress!!.max = 100 // Default upload max 100%
        onProgressChanged()

        setProgress(mProgressVal, mCurrentFileVal)
        setMessage(mMessage)

        setView(view)
    }

    private fun onProgressChanged() {
        if (mViewUpdateHandler != null && !mViewUpdateHandler!!.hasMessages(0)) {
            mViewUpdateHandler!!.sendEmptyMessage(0)
        }
    }

    /**
     * Sets the current progress.
     *
     * @param value the current progress, a value between 0 and [.getMax]
     *
     * @see ProgressBar.setProgress
     */
    fun setProgress(value: Int, currentFile: Int): DbUploadProgressDialog {
        mProgress?.progress = value
        onProgressChanged()
        mProgressVal = value
        mCurrentFileVal = currentFile
        return this
    }

    /**
     * Gets the current progress.
     *
     * @return the current progress, a value between 0 and [.getMax]
     */
    fun getProgress(): Int {
        return if (mProgress != null) {
            mProgress!!.progress
        } else mProgressVal
    }

    /**
     * Gets the maximum allowed progress value. The default value is 100.
     *
     * @return the maximum value
     */
    fun getMaxFiles(): Int {
        return mMaxFileVal
    }

    /**
     * Sets the maximum allowed progress value.
     */
    fun setMaxFiles(max: Int): DbUploadProgressDialog {
        mMaxFileVal = max
        return this
    }

    fun getCurrentFile(): Int {
        return mCurrentFileVal
    }

    /**
     * Sets the drawable to be used to display the progress value.
     *
     * @param d the drawable to be used
     *
     * @see ProgressBar.setProgressDrawable
     */
    fun setProgressDrawable(d: Drawable): DbUploadProgressDialog {
        if (mProgress != null) {
            mProgress!!.progressDrawable = d
        } else {
            mProgressDrawable = d
        }
        return this
    }

    fun setBackgroundColor(color: ColorDrawable): DbUploadProgressDialog {
        if (mView != null) {
            mView.background = color
        }
        return this
    }

    /**
     * Change the format of the small text showing current and maximum units
     * of progress.  The default is "%1d/%2d".
     * Should not be called during the number is progressing.
     * @param format A string passed to [String.format()][String.format];
     * use "%1d" for the current number and "%2d" for the maximum.  If null,
     * nothing will be shown.
     */
    fun setProgressNumberFormat(format: String): DbUploadProgressDialog {
        mProgressNumberFormat = format
        onProgressChanged()
        return this
    }

    /**
     * Change the format of the small text showing the percentage of progress.
     * The default is
     * [NumberFormat.getPercentageInstance().][NumberFormat.getPercentInstance]
     * Should not be called during the number is progressing.
     * @param format An instance of a [NumberFormat] to generate the
     * percentage text.  If null, nothing will be shown.
     */
    fun setProgressPercentFormat(format: NumberFormat): DbUploadProgressDialog {
        mProgressPercentFormat = format
        onProgressChanged()
        return this
    }

    /**
     * Displays an optional message
     * Functions same as the setMessage() in deprecated ProgressDialog class
     * @param message A string object to display on the progress dialog
     **/
    /* Set message on the progress bar. */
    fun setMessage(message: String): DbUploadProgressDialog {
        mMessageView?.text = message
        mMessage = message
        return this
    }

    /**
     * Set progress bar color.
     * Color should be a constant from Color class, eg: Color.RED
     * NB: Not a resId
     * @param color Constant from Color class, eg: Color.RED
     **/
    /* Sets progress bar's color */
    fun setColor(color: Int): DbUploadProgressDialog {
        mProgress?.progressDrawable?.mutate()?.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
        return this
    }

    /**
     * Called when back button is pressed.
     * Should be called in the overridden onBackPressed() of the activity
     * @param superOnBackPressed = {super.onBackPressed()} A block of code to be executed.
     **/
    fun onBackPressed(superOnBackPressed: () -> Unit) {
        if (mView.visibility == View.VISIBLE) {

        } else
            superOnBackPressed.invoke()
    }

    /**
     * Says whether the dialog is cancelable or not.  Default is true.
     * @return value of cancelable
     */
    // fun isCancelable(): Boolean = cancelable

    /**
     * Set message TextView's text color manually.
     * User can also customize [messageTextView][getMessageTextView] directly.
     * @param color ResourceId of text color
     */
    /* Sets text color */
    fun setTextColor(color: Int): DbUploadProgressDialog {
        mMessageView?.setTextColor(ContextCompat.getColor(context, color))
        return this
    }

    /**
     * Set message TextView's size manually.
     * User can also customize [messageTextView][getMessageTextView] directly.
     * @param sizeInSp text size in sp
     */
    /* Sets text size */
    fun setTextSize(sizeInSp: Float): DbUploadProgressDialog {
        mMessageView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeInSp)
        return this
    }

    /**
     * Return message display TextView, so that the user can customize it as per his wish
     * @return Return message display TextView
     */
    fun getMessageTextView(): TextView? = mMessageView

}