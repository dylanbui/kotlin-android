package com.dylanbui.android_library.utils_extentions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun ViewGroup.hide() {
    visibility = View.GONE
}

fun ViewGroup.show() {
    visibility = View.VISIBLE
}

fun ViewGroup.invisible() {
    visibility = View.INVISIBLE
}

fun Context.inflateLayout(layoutResId: Int): View {
    return inflateView(this, layoutResId, null, false)
}

fun Context.inflateLayout(layoutResId: Int, parent: ViewGroup): View {
    return inflateLayout(layoutResId, parent, true)
}

fun Context.inflateLayout(layoutResId: Int, parent: ViewGroup, attachToRoot: Boolean): View {
    return inflateView(this, layoutResId, parent, attachToRoot)
}

private fun inflateView(context: Context, layoutResId: Int, parent: ViewGroup?, attachToRoot: Boolean): View {
    return LayoutInflater.from(context).inflate(layoutResId, parent, attachToRoot)
}

fun View.showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG, f: (Snackbar.() -> Unit) = {}) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun View.showSnackbar(@StringRes message: Int, length: Int = Snackbar.LENGTH_LONG, f: (Snackbar.() -> Unit) = {}) {
    showSnackbar(resources.getString(message), length, f)
}

fun Snackbar.action(action: String, @ColorInt color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}