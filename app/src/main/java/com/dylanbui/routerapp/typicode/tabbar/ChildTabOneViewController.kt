package com.dylanbui.routerapp.typicode.tabbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R

class ChildTabOneViewController : BaseMvpController<ChildTabOneViewAction, ChildTabOnePresenter>(),
    ChildTabOneViewAction {

    override fun setTitle(): String? = "ChildTabOne"

    override fun createPresenter(): ChildTabOnePresenter = ChildTabOnePresenter()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_child_tab_one, container, false)
    }

    override fun onViewBound(view: View) {

    }

    override fun onPreAttach() {
        // -- At here presenter is existed --
        // -- Get action in row => presenter --
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
    }


}

