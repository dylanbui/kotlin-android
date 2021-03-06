package com.dylanbui.android_library.ui_control.progress_dialog

import android.annotation.SuppressLint
import android.app.Activity

import android.content.Context
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
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import com.dylanbui.android_library.R
import java.text.NumberFormat

/***
 * Created by Livin Mathew <mail@livinmathew.me> on 10/3/18.
 */

/**
 * A dialog showing a progress indicator and an optional text message or view.
 * Only a text message or a view can be used at the same time.
 *
 * <p>The dialog can be made cancelable on back key press.</p>
 *
 * <p>The progress range is 0 to {@link #getMax() max}.</p>
 * Base on v0.1.5 : https://github.com/Livin21/MissMe
 * Y tuong la dung View tren top man hinh, co the ap dung cho cac loai hinh khac
 * DucBui 07/04/2020 chinh sua lai ke thua tu AlertDialog
 */

class DbProgressDialog(context: AppCompatActivity,
                       private val mProgressStyle: ProgressStyle = ProgressStyle.SpinnerStyle) : AlertDialog(context) {

    companion object {
        const val INVALID_ID = -1
    }

    sealed class ProgressStyle {
        /**
         * Creates a ProgressDialog with a circular, spinning progress
         * bar. This is the default.
         */
        object SpinnerStyle : ProgressStyle()

        /**
         * Creates a ProgressDialog with a horizontal progress bar.
         */
        object HorizontalStyle : ProgressStyle()

        /**
         * User defined style!
         */
        class CustomStyle(@IdRes val progressView: Int = INVALID_ID,
                          @IdRes val messageView: Int = INVALID_ID,
                          val viewMaker: (Context) -> View) : ProgressStyle()
    }

    private var mProgress: ProgressBar? = null
    private var mMessageView: TextView? = null

    // private var mProgressStyle: ProgressStyle = ProgressStyle.SpinnerStyle
    private var mProgressNumber: TextView? = null
    private var mProgressNumberFormat: String? = null
    private var mProgressPercent: TextView? = null
    private var mProgressPercentFormat: NumberFormat? = null

    private var mMax: Int = 0
    private var mProgressVal: Int = 0
    private var mSecondaryProgressVal: Int = 0
    private var mIncrementBy: Int = 0
    private var mIncrementSecondaryBy: Int = 0
    private var mProgressDrawable: Drawable? = null
    private var mIndeterminateDrawable: Drawable? = null
    private var mMessage: String = ""
    private var mIndeterminate: Boolean = false

    private var mViewUpdateHandler: Handler? = null

    private var mProgressDialogView: View? = null
    private lateinit var mView: View

//    private var cancelable: Boolean = false

    init {

        initFormats()

        if (mMax > 0) setMax(mMax)
        if (mProgressVal > 0) setProgress(mProgressVal)
        if (mSecondaryProgressVal > 0) setSecondaryProgress(mSecondaryProgressVal)
        if (mIncrementBy > 0) incrementProgressBy(mIncrementBy)
        if (mIncrementSecondaryBy > 0) incrementSecondaryProgressBy(mIncrementSecondaryBy)
        if (mProgressDrawable != null) setProgressDrawable(mProgressDrawable!!)
        if (mIndeterminateDrawable != null) setIndeterminateDrawable(mIndeterminateDrawable!!)
        setMessage(mMessage)

        setIndeterminate(mIndeterminate)
    }

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
//        viewDialog = LayoutInflater.from(context).inflate(R.layout.view_dialog_horizontal_progress, null)
//        setView(viewDialog)

        /* Initialize spinner layout as the default layout */
        // spinnerLayout()

        // val style = mProgressStyle
        when (mProgressStyle) {
            ProgressStyle.HorizontalStyle -> horizontalLayout()
            ProgressStyle.SpinnerStyle -> spinnerLayout()
            is ProgressStyle.CustomStyle -> setCustomLayout(mProgressStyle)
        }

        onProgressChanged()

        /* Hide progress dialog initially */
//        dismiss()
//
        setCancelable(false) // Dont allow touch out dismiss

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()



    }

    private fun initFormats() {
        mProgressNumberFormat = "%1d/%2d"
        mProgressPercentFormat = NumberFormat.getPercentInstance()
        mProgressPercentFormat?.maximumFractionDigits = 0
    }

    private fun spinnerLayout() {

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.progress_dialog_spinner, null, false)
        // Default black - 60%
        // view.background = ColorDrawable(Color.parseColor("#99000000"))

        mProgressDialogView = view.findViewById(R.id.progressDialogView)

        mProgress = view.findViewById<View>(R.id.progress) as ProgressBar
        mMessageView = view.findViewById<View>(R.id.message) as TextView
        setMax(mMax)
        setProgress(mProgressVal)
        setIndeterminate(mIndeterminate)
        setMessage(mMessage)

        setView(view)
    }

    private fun setCustomLayout(customProgressStyle: ProgressStyle.CustomStyle) {
        customProgressStyle.viewMaker.invoke(context).let {
            if (customProgressStyle.messageView != INVALID_ID) {
                mMessageView = it.findViewById(customProgressStyle.messageView)
            }
            if (customProgressStyle.progressView != INVALID_ID) {
                mProgress = it.findViewById(customProgressStyle.progressView)
            }
            setView(it)
        }
    }

    private fun horizontalLayout() {

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.progress_dialog_horizontal, null, false)

        /* Use a separate handler to update the text views as they
         * must be updated on the same thread that created them.
         */
        mViewUpdateHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                /* Update the number and percent */
                val progress = mProgress?.progress
                val max = mProgress?.max
                if (mProgressNumberFormat != null) {
                    val format = mProgressNumberFormat
                    mProgressNumber?.text = format?.let { String.format(it, progress, max) }
                } else {
                    mProgressNumber?.text = ""
                }
                if (mProgressPercentFormat != null) {
                    val percent = (progress?.toDouble() ?: 0.0) / (max?.toDouble() ?: 100.0)
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

        setMax(mMax)
        setProgress(mProgressVal)
        setIndeterminate(mIndeterminate)
        setMessage(mMessage)

        setView(view)
    }

//    private fun setView(view: View) {
//        val layoutParams = RelativeLayout.LayoutParams(
//            RelativeLayout.LayoutParams.MATCH_PARENT,
//            RelativeLayout.LayoutParams.MATCH_PARENT
//        )
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
//        mActivity?.addContentView(view, layoutParams)
//        mView = view
//        mProgressDialogView = view.findViewById(R.id.progressDialogView)
//
//        /* If clicked anywhere on the screen except the progress dialog,
//         * the progress dialog must dismiss depending upon the value of cancelable
//         */
//        mView.setOnClickListener {
//            if (cancelable)
//                dismiss()
//        }
//
//        /* Left empty purposefully. To detach progressDialog and
//         * its contents from layout's click listener
//         */
//        mProgressDialogView?.setOnClickListener {}
//
//        mView.visibility = View.GONE
//    }

    private fun onProgressChanged() {
        if (mProgressStyle == ProgressStyle.HorizontalStyle) {
            if (mViewUpdateHandler != null && !mViewUpdateHandler!!.hasMessages(0)) {
                mViewUpdateHandler!!.sendEmptyMessage(0)
            }
        }
    }

    /**
     * Sets the current progress.
     *
     * @param value the current progress, a value between 0 and [.getMax]
     *
     * @see ProgressBar.setProgress
     */
    fun setProgress(value: Int): DbProgressDialog {
        mProgress?.progress = value
        onProgressChanged()
        mProgressVal = value
        return this
    }

    /**
     * Sets the secondary progress.
     *
     * @param secondaryProgress the current secondary progress, a value between 0 and
     * [.getMax]
     *
     * @see ProgressBar.setSecondaryProgress
     */
    fun setSecondaryProgress(secondaryProgress: Int): DbProgressDialog {
        if (mProgress != null) {
            mProgress?.secondaryProgress = secondaryProgress
            onProgressChanged()
        } else {
            mSecondaryProgressVal = secondaryProgress
        }
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
     * Gets the current secondary progress.
     *
     * @return the current secondary progress, a value between 0 and [.getMax]
     */
    fun getSecondaryProgress(): Int {
        return if (mProgress != null) {
            mProgress!!.secondaryProgress
        } else mSecondaryProgressVal
    }

    /**
     * Gets the maximum allowed progress value. The default value is 100.
     *
     * @return the maximum value
     */
    fun getMax(): Int {
        return if (mProgress != null) {
            mProgress!!.max
        } else mMax
    }

    /**
     * Sets the maximum allowed progress value.
     */
    fun setMax(max: Int): DbProgressDialog {
        if (mProgress != null) {
            mProgress!!.max = max
            onProgressChanged()
        }
        mMax = max
        return this
    }

    /**
     * Increments the current progress value.
     *
     * @param diff the amount by which the current progress will be incremented,
     * up to [.getMax]
     */
    fun incrementProgressBy(diff: Int) {
        if (mProgress != null) {
            mProgress!!.incrementProgressBy(diff)
            onProgressChanged()
        } else {
            mIncrementBy += diff
        }
    }

    /**
     * Increments the current secondary progress value.
     *
     * @param diff the amount by which the current secondary progress will be incremented,
     * up to [.getMax]
     */
    fun incrementSecondaryProgressBy(diff: Int) {
        if (mProgress != null) {
            mProgress!!.incrementSecondaryProgressBy(diff)
            onProgressChanged()
        } else {
            mIncrementSecondaryBy += diff
        }
    }

    /**
     * Sets the drawable to be used to display the progress value.
     *
     * @param d the drawable to be used
     *
     * @see ProgressBar.setProgressDrawable
     */
    fun setProgressDrawable(d: Drawable): DbProgressDialog {
        if (mProgress != null) {
            mProgress!!.progressDrawable = d
        } else {
            mProgressDrawable = d
        }
        return this
    }

    /**
     * Sets the drawable to be used to display the indeterminate progress value.
     *
     * @param d the drawable to be used
     *
     * @see ProgressBar.setProgressDrawable
     * @see .setIndeterminate
     */
    fun setIndeterminateDrawable(d: Drawable): DbProgressDialog {
        if (mProgress != null) {
            mProgress!!.indeterminateDrawable = d
        } else {
            mIndeterminateDrawable = d
        }
        return this
    }

    /**
     * Change the indeterminate mode for this ProgressDialog. In indeterminate
     * mode, the progress is ignored and the dialog shows an infinite
     * animation instead.
     *
     *
     * **Note:** A ProgressDialog with style [.STYLE_SPINNER]
     * is always indeterminate and will ignore this setting.
     *
     * @param indeterminate true to enable indeterminate mode, false otherwise
     *
     * @see .setProgressStyle
     */
    fun setIndeterminate(indeterminate: Boolean): DbProgressDialog {
        if (mProgress != null) {
            mProgress!!.isIndeterminate = indeterminate
        }
        mIndeterminate = indeterminate

        /* Hide progress display TextViews */
        if (indeterminate) {
            mProgressNumberFormat = null
            mProgressPercentFormat = null
        } else
            initFormats()

        return this
    }

    /**
     * Whether this ProgressDialog is in indeterminate mode.
     *
     * @return true if the dialog is in indeterminate mode, false otherwise
     */
    fun isIndeterminate(): Boolean {
        return if (mProgress != null) {
            mProgress!!.isIndeterminate
        } else mIndeterminate
    }

    /**
     * Sets the style of this ProgressDialog, either [ProgressStyle.HorizontalStyle] or
     * [ProgressStyle.SpinnerStyle] or [ProgressStyle.CustomStyle].
     * The default is [ProgressStyle.SpinnerStyle].
     *
     *
     * **Note:** A ProgressDialog with style [ProgressStyle.SpinnerStyle]
     * is always indeterminate and will ignore the [ indeterminate][.setIndeterminate] setting.
     *
     * @param style the style of this ProgressDialog
     */
//    fun setProgressStyle(style: ProgressStyle): DbProgressDialog {
//        mProgressStyle = style
////        when (style) {
////            ProgressStyle.HorizontalStyle -> horizontalLayout()
////            ProgressStyle.SpinnerStyle -> spinnerLayout()
////            is ProgressStyle.CustomStyle -> setCustomLayout(style)
////        }
//        return this
//    }

    /**
     * Change the format of the small text showing current and maximum units
     * of progress.  The default is "%1d/%2d".
     * Should not be called during the number is progressing.
     * @param format A string passed to [String.format()][String.format];
     * use "%1d" for the current number and "%2d" for the maximum.  If null,
     * nothing will be shown.
     */
    fun setProgressNumberFormat(format: String): DbProgressDialog {
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
    fun setProgressPercentFormat(format: NumberFormat): DbProgressDialog {
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
    fun setMessage(message: String): DbProgressDialog {
        mMessageView?.text = message
        mMessage = message
        return this
    }

    /**
     * Create and show progress dialog
     */
    /* Display progress dialog */
//    fun show(animate: Boolean = true): DbProgressDialog {
//        mView.setVisible(true, animate)
//        return this
//    }

    /**
     * Dismiss progress dialog
     **/
    /* Hide progress dialog */
//    fun dismiss(animate: Boolean = true) {
//        mView.setVisible(false, animate) {
//            setProgress(0)
//        }
//    }

    /**
     * Sets whether the dialog is cancelable or not.  Default is true.
     * @param cancelable A boolean which determines if the dialog can be dismissed by the user
     **/
    /* Toggles value of cancelable */
//    fun setCancelable(cancelable: Boolean): DbProgressDialog {
//        this.cancelable = cancelable
//        return this
//    }

    /**
     * Set progress bar color.
     * Color should be a constant from Color class, eg: Color.RED
     * NB: Not a resId
     * @param color Constant from Color class, eg: Color.RED
     **/
    /* Sets progress bar's color */
    fun setColor(color: Int): DbProgressDialog {
        if (mProgressStyle == ProgressStyle.HorizontalStyle)
            mProgress?.progressDrawable?.mutate()?.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
        else
            mProgress?.indeterminateDrawable?.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
        return this
    }

    /**
     * Called when back button is pressed.
     * Should be called in the overridden onBackPressed() of the activity
     * @param superOnBackPressed = {super.onBackPressed()} A block of code to be executed.
     **/
    fun onBackPressed(superOnBackPressed: () -> Unit) {
        if (mView.visibility == View.VISIBLE) {
//            if (cancelable)
//                dismiss()
        } else
            superOnBackPressed.invoke()
    }

    /**
     * Says whether the dialog is cancelable or not.  Default is true.
     * @return value of cancelable
     */
//    fun isCancelable(): Boolean = cancelable

    /**
     * Set message TextView's text color manually.
     * User can also customize [messageTextView][getMessageTextView] directly.
     * @param color ResourceId of text color
     */
    /* Sets text color */
    fun setTextColor(color: Int): DbProgressDialog {
        mMessageView?.setTextColor(ContextCompat.getColor(context, color))
        return this
    }

    /**
     * Set message TextView's size manually.
     * User can also customize [messageTextView][getMessageTextView] directly.
     * @param sizeInSp text size in sp
     */
    /* Sets text size */
    fun setTextSize(sizeInSp: Float): DbProgressDialog {
        mMessageView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeInSp)
        return this
    }

    /**
     * Return message display TextView, so that the user can customize it as per his wish
     * @return Return message display TextView
     */
    fun getMessageTextView(): TextView? = mMessageView

    fun setBackgroundColor(color: ColorDrawable): DbProgressDialog {
        if (mView != null) {
            mView.background = color
        }
        return this
    }

}