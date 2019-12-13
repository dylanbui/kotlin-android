package com.dylanbui.routerapp.demofragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.dylanbui.routerapp.MainActivity
import com.dylanbui.routerapp.R

class ThirdViewController : Controller()
{
    lateinit var btnFirst: Button
    lateinit var btnNext: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View
    {
        var view: View = inflater.inflate(R.layout.controller_first, container, false)
        onViewBound(view)
        return view
    }

    private fun setTitle(): String = "Title Third 3 ne"

    private fun onViewBound(view: View)
    {
        println("Dylan: onViewBound")

        btnFirst = view.findViewById(R.id.btnFirst)
        btnFirst.setOnClickListener { _ ->
            Toast.makeText(activity, "Day la man hinh thu 3", Toast.LENGTH_LONG).show()
        }
        btnFirst.text = "${btnFirst.text} 333"

        btnNext = view.findViewById(R.id.btnNextControl)
        btnNext.setOnClickListener { _ ->
            // Toast.makeText(applicationContext, "show ra loi", Toast.LENGTH_LONG).show()
            Toast.makeText(activity, "Day la man hinh thu 3", Toast.LENGTH_LONG).show()
        }
        btnNext.text = "${btnNext.text} 3 ne"
    }

    override fun onChangeStarted(
        changeHandler: ControllerChangeHandler,
        changeType: ControllerChangeType
    ) {
        super.onChangeStarted(changeHandler, changeType)

        // Chu y la thang nay cung co the duoc goi lai nhieu lan

        // add menu nen de trong lop nay
        // Thu tu chay
//        I/System.out: Dylan: onViewBound
//        Dylan: onChangeStarted
//        I/System.out: Dylan: onAttach


        println("Dylan: onChangeStarted")

//        var mainActivity = activity as? MainActivity
//        mainActivity?.let {
//            // it.enableToolBar(false)
//            it.setToolBarTitle(setTitle())
//            it.enableUpArrow(router.backstackSize > 1)
//
//        }
    }

    override fun onAttach(view: View)
    {
        super.onAttach(view)

        println("Dylan: onAttach")


    }

    override fun onDetach(view: View) {
        super.onDetach(view)

        var mainActivity = activity as? MainActivity

        mainActivity?.let {
            //it.enableToolBar(true)
        }
    }
}