package com.dylanbui.routerapp.demofragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.dylanbui.android_library.livedatabus.DbEventLiveData
import com.dylanbui.android_library.livedatabus.DbLiveDataBus
import com.dylanbui.routerapp.MainActivity
import com.dylanbui.routerapp.R

class FirstViewController : Controller()
{
    lateinit var btnFirst: Button
    lateinit var btnNext: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        var view: View = inflater.inflate(R.layout.controller_first, container, false)
        onViewBound(view)
        return view
    }

    private fun setTitle(): String = "Title first"

    private fun onViewBound(view: View)
    {
        btnFirst = view.findViewById(R.id.btnFirst)
        btnFirst.setOnClickListener { _ ->
            router.pushController(RouterTransaction.with(SecondViewController())
                .pushChangeHandler(HorizontalChangeHandler())
                .popChangeHandler(HorizontalChangeHandler()))

//            router.pushController(RouterTransaction.with(SecondViewController())
//                .pushChangeHandler(FadeChangeHandler())
//                .popChangeHandler(FadeChangeHandler()))

        }

        btnNext = view.findViewById(R.id.btnNextControl)
        btnNext.setOnClickListener { _ ->
            // Toast.makeText(applicationContext, "show ra loi", Toast.LENGTH_LONG).show()
            Toast.makeText(activity, "show ra loi", Toast.LENGTH_LONG).show()
        }

    }

    override fun onAttach(view: View)
    {
        super.onAttach(view)

        var mainActivity = activity as? MainActivity

        mainActivity?.let {
            it.setToolBarTitle(setTitle())
            it.enableUpArrow(router.backstackSize > 1)
        }

    }
}