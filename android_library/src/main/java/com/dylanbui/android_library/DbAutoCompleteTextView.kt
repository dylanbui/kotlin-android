package com.dylanbui.android_library

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar

// http://makovkastar.github.io/blog/2014/04/12/android-autocompletetextview-with-suggestions-from-a-web-service/

class DbAutoCompleteTextView(context: Context?, attrs: AttributeSet?) : AutoCompleteTextView(context, attrs) {

    private var mAutoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY
    private var mLoadingIndicator: ProgressBar? = null

    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super@DbAutoCompleteTextView.performFiltering(
                msg.obj as CharSequence,
                msg.arg1
            )
        }
    }

    fun setLoadingIndicator(progressBar: ProgressBar?) {
        mLoadingIndicator = progressBar
    }

    fun setAutoCompleteDelay(autoCompleteDelay: Int) {
        mAutoCompleteDelay = autoCompleteDelay
    }

    override fun performFiltering(text: CharSequence, keyCode: Int) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator?.visibility = View.VISIBLE
        }
        mHandler.removeMessages(MESSAGE_TEXT_CHANGED)
        mHandler.sendMessageDelayed(
            mHandler.obtainMessage(
                MESSAGE_TEXT_CHANGED,
                text
            ), mAutoCompleteDelay.toLong()
        )
    }

    override fun onFilterComplete(count: Int) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator?.visibility = View.GONE
        }
        super.onFilterComplete(count)
    }

    companion object {
        private const val MESSAGE_TEXT_CHANGED = 100
        private const val DEFAULT_AUTOCOMPLETE_DELAY = 750
    }
}