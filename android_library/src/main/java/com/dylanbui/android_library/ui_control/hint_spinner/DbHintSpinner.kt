package com.dylanbui.android_library.ui_control.hint_spinner

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner

/**
 * Provides methods to work with a hint element.
 *
 * Base on : https://github.com/srodrigo/Android-Hint-Spinner
 */
class DbHintSpinner<T>(
    private val spinner: Spinner,
    private val adapter: DbHintAdapter<T>,
    private val callback: Callback<T>?
) {
    /**
     * Used to handle the spinner events.
     *
     * @param <T> Type of the data used by the spinner
    </T> */
    interface Callback<T> {
        /**
         * When a spinner item has been selected.
         *
         * @param position Position selected
         * @param itemAtPosition Item selected
         */
        fun onItemSelected(position: Int, itemAtPosition: T)
    }

    /**
     * Initializes the hint spinner.
     *
     * By default, the hint is selected when calling this method.
     */
    fun init() {
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                Log.d(TAG, "position selected: $position")
                checkNotNull(callback) { "callback cannot be null" }
                if (!isHintPosition(position)) {
                    val item = spinner.getItemAtPosition(position)
                    callback.onItemSelected(position, item as T)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d(
                    TAG,
                    "Nothing selected"
                )
            }
        }
        selectHint()
    }

    private fun isHintPosition(position: Int): Boolean {
        return position == adapter.hintPosition
    }

    /**
     * Selects the hint element.
     */
    fun selectHint() {
        spinner.setSelection(adapter.hintPosition)
    }

    companion object {
        private val TAG = DbHintSpinner::class.java.simpleName
    }

}