package com.dylanbui.routerapp.typicode.tabbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.dylanbui.routerapp.R

private const val CONDUCT_TEXT = "CONDUCT_TEXT"
private const val CONDUCT_BG_COLOR = "CONDUCT_BG_COLOR"

class ChildController(text: String = "", backgroundColor: Int = 0) : Controller(Bundle().apply {
    putString(CONDUCT_TEXT, text)
    putInt(CONDUCT_BG_COLOR, backgroundColor)

}) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_main_tabbar_demo_content, container, false)
        view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.blue_text_color))
        // view.setBackgroundColor(Color.BLUE)

        val txtView = view.findViewById<TextView>(R.id.txtContent)
        txtView.text = "${args.getString(CONDUCT_TEXT)} : title"
        val button = view.findViewById<TextView>(R.id.btnDemo)
        // button.text = args.getString(CONDUCT_TEXT + "Button")
        button.text = "${args.getString(CONDUCT_TEXT)} : Button"
        button.setOnClickListener {
            router.pushController(RouterTransaction.with(ConductorController(args.getString(CONDUCT_TEXT) + " child")))
        }

        var bgColor = args.getInt(CONDUCT_BG_COLOR)
        bgColor = ContextCompat.getColor(activity!!, bgColor)
        view.setBackgroundColor(bgColor)
        return view
    }

}