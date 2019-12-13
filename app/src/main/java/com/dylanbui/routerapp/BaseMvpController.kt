package com.dylanbui.routerapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.hannesdorfmann.mosby3.mvp.conductor.MvpController

interface ActionBarProvider {
    fun supportActionBar(): ActionBar?
}

abstract class BaseMvpController<V: MvpView, P: MvpPresenter<V>> : MvpController<V, P>
{
    // private lateinit var unbinder: Unbinder

    protected constructor() {

    }

    protected constructor(args: Bundle?) : super(args) {

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
        // unbinder = ButterKnife.bind(this, view)
        onViewBound(view)
        return view
    }

    protected abstract fun onViewBound(view: View)

    override fun onAttach(view: View)
    {
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
        // unbinder.unbind()
    }

    protected abstract fun setTitle(): String?

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