package com.dylanbui.android_library.mvp_structure

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBar
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.dylanbui.android_library.DbError
import com.dylanbui.android_library.DbMessageEvent
import com.dylanbui.android_library.mvp_structure.conductor.DbMvpController
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

interface ActionBarProvider {
    fun supportActionBar(): ActionBar?
}

@Suppress("OverridingDeprecatedMember", "DEPRECATION")
@SuppressWarnings("deprecation", "unused")
abstract class DbBaseMvpController<V: MvpView, P: MvpPresenter<V>> : DbMvpController<V, P>, DbBaseMvpView
{
    open var nav: DbNavigation? = null

    // Inject dependencies once per life of Controller
    private val injectRunOnce by lazy { onPreAttach() }

    protected open fun setTitle(): String? = null

    // open fun showToolbar() : Boolean  = true

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        Log.d("onCreateView", this.javaClass.name)
        // -- inflateView for this Controller --
        val view: View = inflateView(inflater, container)
        // progressView = view.findViewById(R.id.progressView)
//        activity?.let {
//            this.progressDialog = DbUtils.makeProgressDialog(it, getStringResource(R.string.loading_title))
//        }
        // toolbar = view.findViewById(R.id.toolbar)
//        toolbar?.title = setTitle()
//        this.enableBackButton()
        // -- Register Event Bus , At OnCreateView
        EventBus.getDefault().register(this)
        // -- Blind View for this Controller --
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

//        this.mainActivity = activity as? MainActivity
//        this.mainActivity?.let {
//            it.setToolBarTitle(setTitle())
//            it.enableUpArrow(router.backstackSize > 1)
//        }

    }

    override fun onDestroyView(view: View)
    {
        // -- Unregister Event Bus
        EventBus.getDefault().unregister(this)
        super.onDestroyView(view)
    }

    // -- Interface BaseMvpView --

    override fun getStringResource(resourceId: Int): String {
        // return activity?.getString(resourceId) ?: ""
        return ""
    }

    override fun showLoading() {
        // this.progressDialog?.show()
    }

    override fun hideLoading() {
        // this.progressDialog?.hide()
    }

    override fun showToast(text: String) {

    }

    override fun showError(error: DbError) {

    }

    // -- --

//    protected open fun enableBackButton(enable: Boolean = true) {
//        // dLog("router.backstackSize = ${router.backstackSize}")
//        toolbar?.let {
//            if (router.backstackSize > 1) {
//                it.setNavigationIcon(R.drawable.ic_arrow_back)
//                it.setNavigationOnClickListener { _ ->
//                    if (router.backstackSize > 1) {
//                        router.popCurrentController()
//                    }
//                }
//            } else {
//                it.navigationIcon = null
//            }
//            // Hide back button
//            if (!enable) {
//                it.navigationIcon = null
//            }
//        }
//    }

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

    fun hideKeyboardFrom(view: View) {
        view.clearFocus()
        val imm =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(mes: DbMessageEvent) {
    }

}