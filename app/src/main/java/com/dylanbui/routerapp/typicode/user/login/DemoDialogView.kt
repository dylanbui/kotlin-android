package com.dylanbui.routerapp.typicode.user.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.bluelinelabs.conductor.Controller
import com.dylanbui.routerapp.R

class DemoDialogView: Controller()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var view = inflater.inflate(R.layout.dialog_fab, container, false)
        onViewBound(view)
        return view
    }


    fun onViewBound(view: View) {

        var tvTitle = view.findViewById<TextView>(R.id.tv_title)
        tvTitle.text = "Conductor"
        var tvDescription = view.findViewById<TextView>(R.id.tv_description)
        tvDescription.text = "A small, yet full-featured framework that allows building View-based Android applications. https://github.com/bluelinelabs/Conductor"

        var dismiss = view.findViewById<Button>(R.id.dismiss)
        dismiss.setOnClickListener() {
            router.popController(this)
        }

        var layout = view.findViewById<ViewGroup>(R.id.dialog_window)
        layout.setOnClickListener {
            router.popController(this)
        }

    }


    override fun handleBack(): Boolean {
        return super.handleBack()
    }
}