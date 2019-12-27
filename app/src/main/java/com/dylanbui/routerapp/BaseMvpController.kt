package com.dylanbui.routerapp

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.dylanbui.android_library.utils.Utils
import com.dylanbui.routerapp.utils.DbNavigation
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.hannesdorfmann.mosby3.mvp.conductor.MvpController

interface ActionBarProvider {
    fun supportActionBar(): ActionBar?
}

@Suppress("OverridingDeprecatedMember", "DEPRECATION")
@SuppressWarnings("deprecation", "unused")
abstract class BaseMvpController<V: MvpView, P: MvpPresenter<V>> : MvpController<V, P>, BaseMvpView
{
    open var nav: DbNavigation? = null

    protected var toolbar: Toolbar? = null
    protected var progressView: ViewGroup? = null // Loading for control
    protected var progressDialog: AlertDialog? = null // Loading for page

    // Inject dependencies once per life of Controller
    private val injectRunOnce by lazy { onPreAttach() }

    protected open fun setTitle(): String? = null

    protected constructor(): this(null) {

    }

    protected constructor(args: Bundle?) : super(args) {
        // Van giu controller in Memory, khi push new controller
        retainViewMode = RetainViewMode.RETAIN_DETACH
    }

    // Note: This is just a quick demo of how an ActionBar *can* be accessed, not necessarily how it *should*
    // be accessed. In a production app, this would use Dagger instead.
    protected fun getActionBar(): ActionBar?
    {
        var actionBarProvider: ActionBarProvider? = activity as? ActionBarProvider
        return if (actionBarProvider != null) actionBarProvider?.supportActionBar() else null
    }

    protected abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup): View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View
    {
        val view: View = inflateView(inflater, container)
        progressView = view.findViewById(R.id.progressView)
        activity?.let {
            this.progressDialog = Utils.makeProgressDialog(it, getStringResource(R.string.loading_title))
        }
        toolbar = view.findViewById(R.id.toolbar)
        toolbar?.title = setTitle()
        this.enableBackButton()
        onViewBound(view)
        return view
    }

    protected abstract fun onViewBound(view: View)

    protected abstract fun onPreAttach()

    override fun onAttach(view: View)
    {
        // Call inject variable
        injectRunOnce

        super.onAttach(view)

        var mainActivity = activity as? MainActivity

        mainActivity?.let {
            it.setToolBarTitle(setTitle())
            it.enableUpArrow(router.backstackSize > 1)
        }

    }

    override fun onDestroyView(view: View)
    {
        super.onDestroyView(view)
    }

    // -- Interface BaseMvpView --

    override fun getStringResource(resourceId: Int): String {
        return activity?.getString(resourceId) ?: ""
    }

    override fun showLoading() {
        this.progressDialog?.show()
    }

    override fun hideLoading() {
        this.progressDialog?.hide()
    }

    // -- --

    protected open fun enableBackButton(enable: Boolean = true) {
        // dLog("router.backstackSize = ${router.backstackSize}")
        toolbar?.let {
            if (router.backstackSize > 1) {
                it.setNavigationIcon(R.drawable.ic_arrow_back)
                it.setNavigationOnClickListener { _ ->
                    if (router.backstackSize > 1) {
                        router.popCurrentController()
                    }
                }
            } else {
                it.navigationIcon = null
            }
            // Hide back button
            if (!enable) {
                it.navigationIcon = null
            }
        }
    }

    // -- Make animation change controller
    override fun onChangeStarted(changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
        super.onChangeStarted(changeHandler, changeType)
        // toolbar?.isEnabled = false
        // toolbar?.visibility = View.INVISIBLE
    }

    override fun onChangeEnded(changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
        super.onChangeEnded(changeHandler, changeType)
        // toolbar?.isEnabled = true
        // toolbar?.visibility = View.VISIBLE
        // val animation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right)
        // toolbar?.startAnimation(animation)
    }

    // Keyboard Manager
    fun hideKeyboard() {
        activity?.let {
            val imm = it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = it.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard() {
        activity?.let {
            val imm = it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        }
    }

    // Get title from parentController
//    protected fun setTitle() {
//        val vclParent = parentController
//        while (vclParent != null)
//        {
//            if (vclParent is Controller && (vclParent as Controller).getTitle() != null)
//            {
//                return
//            }
//            vclParent = vclParent.parentController
//        }
//        val title = getTitle()
//        val actionBar = getActionBar()
//        if (title != null && actionBar != null)
//        {
//            actionBar.setTitle(title)
//        }
//    }

}