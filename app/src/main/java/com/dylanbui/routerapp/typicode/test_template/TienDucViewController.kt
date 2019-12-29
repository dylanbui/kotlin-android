package com.dylanbui.routerapp.typicode.test_template

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R

class TienDucViewController : BaseMvpController<TienDucActionView, TienDucPresenter>(),
    TienDucActionView {

    override fun setTitle(): String? = "TienDuc"

    override fun createPresenter(): TienDucPresenter = TienDucPresenter()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_tien_duc, container, false)
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

