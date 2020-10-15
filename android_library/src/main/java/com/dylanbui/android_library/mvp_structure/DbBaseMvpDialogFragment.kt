package com.dylanbui.android_library.mvp_structure

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dylanbui.android_library.DbError
import com.hannesdorfmann.mosby3.mvp.MvpDialogFragment
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView



/**
 * Created by IntelliJ IDEA.
 * User: Dylan Bui
 * Email: duc@propzy.com
 * Date: 9/16/20
 * To change this template use File | Settings | File and Code Templates.
 */

interface DbDefaultViewAction : DbBaseMvpView { }
class DbDefaultPresenter : DbBaseMvpPresenter<DbDefaultViewAction>() { }

// Only for Simple DialogFragment
abstract class DbBaseDialogFragment : DbBaseMvpDialogFragment<DbDefaultViewAction, DbDefaultPresenter>(), DbDefaultViewAction {
    override fun createPresenter() = DbDefaultPresenter()
}

@Suppress("OverridingDeprecatedMember", "DEPRECATION")
@SuppressWarnings("deprecation", "unused")
abstract class DbBaseMvpDialogFragment<V: MvpView, P: MvpPresenter<V>> : MvpDialogFragment<V, P>(), DbBaseMvpView {

    abstract val inflateViewLayoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create variable
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(inflateViewLayoutId, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val window: Window? = dialog?.window
        //val attributes: ViewGroup.LayoutParams = window?.attributes
        //must setBackgroundDrawable(TRANSPARENT) in onActivityCreated()
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun dismiss() {
        hideKeyboard()
        super.dismiss()
    }

    open fun hideKeyboard() {
        activity?.let { view?.let { v -> hideKeyboardFrom(it, v) } }
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        view.clearFocus()
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    open fun show(act: AppCompatActivity) {
        val tag = this::class.java.name
        val ft = act.supportFragmentManager.beginTransaction()
        val prev = act.supportFragmentManager.findFragmentByTag(tag)
        prev?.let {
            ft.remove(it)
        }
        ft.addToBackStack(null)

        this.show(ft, tag)
    }

    override fun getStringResource(resourceId: Int): String {
        return getString(resourceId)
    }

    override fun getStringQuantity(resourceId: Int, numberCount: Int, vararg text: String): String {
        return getString(resourceId)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showToast(text: String) {
        activity?.let {
            Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
        }
    }

    override fun showError(error: DbError) {
        showToast(error.errorMessage)
    }

    override fun showProgressView() {

    }

    override fun hideProgressView() {

    }
}