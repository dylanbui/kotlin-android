package com.dylanbui.routerapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

@SuppressLint("all")
object EditTextUtils {

    fun setupEditTextUI(view: View?, activity: Activity) {
        try {
            if (view == null)
                return
            // Set up touch listener for non-text box views to hide keyboard.
            if (view !is EditText) {
                view.setOnTouchListener { v, event ->
                    hideSoftKeyboard(activity)
                    false
                }
            }

            //If a layout container, iterate over children and seed recursion.
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val innerView = view.getChildAt(i)
                    setupEditTextUI(innerView, activity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

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

    fun requestFocus(editText: EditText, activity: Activity) {
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

}