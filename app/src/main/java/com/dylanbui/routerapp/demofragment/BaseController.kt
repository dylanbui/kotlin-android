package com.dylanbui.routerapp.demofragment


import android.app.Activity
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bluelinelabs.conductor.Controller
import com.dylanbui.routerapp.ActionBarProvider
import com.dylanbui.routerapp.MainActivity
import com.dylanbui.routerapp.R
import kotlinx.android.extensions.LayoutContainer


abstract class BaseController: Controller(), LayoutContainer {
    private var _containerView: View? = null
    override val containerView: View?
        get() = _containerView

    protected abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup): View
    protected open fun enableToolbarForThisController(): Boolean = true
    protected open fun hasOptionsMenu(): Boolean = false

    var toolbar: Toolbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        val view = inflateView(inflater, container).also {
            // applicationContext
            // Khong can thiet
            //context = it.context
            _containerView = it
        }

        view.findViewById<Toolbar>(R.id.toolbar)?.let {
            toolbar = it
        }
        onViewBound(view)
        return view
    }

    protected open fun onViewBound(view: View) {

        var mainActivity = activity as? MainActivity
        mainActivity?.let {
            this.toolbar = it.toolbar
//            it.setToolBarTitle(setTitle())
//            it.enableUpArrow(router.backstackSize > 1)
        }

        setHasOptionsMenu(hasOptionsMenu())
    }



    override fun onAttach(view: View) {
        super.onAttach(view)
        toolbar?.let {
            setSupportActionBar(it)
            enableToolBar(this.enableToolbarForThisController())
            enableUpArrow(router.backstackSize > 1)
            setTitle()
        }

//        if (router.backstackSize > 1) {
//            toolbar_custom?.setNavigationIcon(R.drawable.ic_arrow_back)
//            toolbar_custom?.setNavigationOnClickListener {
//                // router.pop()
//                router.popCurrentController()
//            }
//        }

    }

    fun enableUpArrow(enabled: Boolean) {
        if (enabled) {
            toolbar?.setNavigationIcon(R.drawable.ic_arrow_back)
            toolbar?.setNavigationOnClickListener { _ ->
                if (router.backstackSize > 1) {
                    router.popCurrentController()
                    // Toast.makeText(this, "setNavigationOnClickListener", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            toolbar?.navigationIcon = null
        }
    }

    fun enableToolBar(enabled: Boolean) {
        if (enabled) {
            toolbar?.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
            toolbar?.startAnimation(animation)
        } else {
            toolbar?.visibility = View.GONE
            val animation = AnimationUtils.loadAnimation(activity, R.anim.fade_out)
            toolbar?.startAnimation(animation)
        }
    }

    protected fun getActionBar(): ActionBar? {
        val actionBarProvider = activity as ActionBarProvider?
        return actionBarProvider?.supportActionBar()
    }

    protected fun setTitle() {
//        val title = getTitle() ?: ""
//        toolbar_custom?.let {
//            it.title = title
//        }
        var txtTitle = getToolbarTitle() as? TextView
        val title = getTitle() ?: ""
        txtTitle?.let {
            it.text = title
            val animation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right)
            it.startAnimation(animation)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_empty, menu)
    }

    protected open fun getTitle(): String? {
        return null
    }

    fun getString(id: Int): String {
        return activity?.getString(id) ?: ""
    }

    fun getString(id: Int, vararg formatArgs: String): String {
        return activity?.getString(id, *formatArgs) ?: ""
    }

    // Keyboard
    fun hideKeyboard() {
        activity?.let {
                activity ->
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard() {
        activity?.let {
                activity ->
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        }
    }

    fun setSupportActionBar(toolbar: Toolbar) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun getToolbarTitle(): View? {
        val childCount = toolbar?.childCount ?: 0
        for (i in 0 until childCount) {
            val child: View? = toolbar?.getChildAt(i)
            if (child is TextView) {
                return child
            }
        }
        return null
    }
}