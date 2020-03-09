package com.dylanbui.android_library.utils

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import com.dylanbui.android_library.dialog_plus.DialogPlus
import com.dylanbui.android_library.dialog_plus.DialogPlusBuilder
import com.dylanbui.android_library.dialog_plus.ViewHolder
import kotlin.system.exitProcess

open abstract class DbDialogAbstract(private val baseContext: Context) {

    private val builder: DialogPlusBuilder = DialogPlus.newDialog(baseContext)
    private var dialog: DialogPlus? = null

    protected abstract fun inflateViewId(): Int

    protected abstract fun configurationBuilder(dialogBuilder: DialogPlusBuilder)

    protected abstract fun onViewBound(view: View)

    init {
        val holder = ViewHolder(inflateViewId())
        builder.setContentHolder(holder)

        // -- Default config --
        builder.isCancelable = true

//        Gravity.TOP
//        Gravity.CENTER
//        Gravity.BOTTOM
        builder.setGravity(Gravity.BOTTOM)
        builder.isExpanded = false
        builder.overlayBackgroundResource = android.R.color.transparent
    }

    protected fun create(): DbDialogAbstract {
        configurationBuilder(builder)

        // -- Make dialog --
        dialog = builder.create()

        // -- Bind action --
        onViewBound(dialog!!.holderView)

        return this
    }

    open fun show() {
        if (dialog == null) {
            Log.e("DbDialogAbstract", "Error: call create() first")
            exitProcess(0)
        }
        dialog?.show()
    }

    open fun dismiss() {
        if (dialog == null) {
            Log.e("DbDialogAbstract", "Error: call create() first")
            exitProcess(0)
        }
        dialog?.dismiss()
    }
}