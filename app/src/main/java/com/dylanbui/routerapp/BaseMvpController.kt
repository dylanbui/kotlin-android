package com.dylanbui.routerapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import com.dylanbui.routerapp.typicode.DbNavigation
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.hannesdorfmann.mosby3.mvp.conductor.MvpController

interface ActionBarProvider {
    fun supportActionBar(): ActionBar?
}

@Suppress("OverridingDeprecatedMember", "DEPRECATION")
abstract class BaseMvpController<V: MvpView, P: MvpPresenter<V>> : MvpController<V, P>, BaseMvpView
{
    open var nav: DbNavigation? = null

    protected var progressView: ViewGroup? = null

    // Inject dependencies once per life of Controller
    private val injectRunOnce by lazy { onPreAttach() }

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

    protected abstract fun setTitle(): String?

    // -- Interface BaseMvpView --

    override fun getStringResource(resourceString: Int): String = ""

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

//    protected fun setTitle(strTitle: String)
//    {
//        val actionBar = getActionBar()
//        actionBar?.let {
//            it.title = strTitle
//        }
//    }

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
//    protected fun getTitle():String {
//        return null
//    }

}