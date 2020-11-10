package com.dylanbui.routerapp.demofragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.dylanbui.android_library.ui_control.hint_spinner.DbHintAdapter
import com.dylanbui.android_library.ui_control.hint_spinner.DbHintSpinner
import com.dylanbui.android_library.ui_control.hint_spinner.DbSpinner
import com.dylanbui.android_library.utils.dLog
import com.dylanbui.android_library.utils.toast
import com.dylanbui.routerapp.MainActivity
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.ui_controller.SpinnerItem
import com.tiper.MaterialSpinner
import com.tiper.MaterialSpinner.OnItemSelectedListener
import kotlinx.android.synthetic.main.controller_first.view.*
import pyxis.uzuki.live.hintablespinner.HintableSpinner


class FirstViewController : Controller()
{
    lateinit var btnFirst: Button
    lateinit var btnNext: Button
    lateinit var btnSave: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        var view: View = inflater.inflate(R.layout.controller_first, container, false)
        onViewBound(view)
        return view
    }

    private fun setTitle(): String = "Title first"

    @SuppressLint("WrongConstant")
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

        view.addr_edittext?.setText("ad \n sddff\n aasasa\n dsfsfdsd")
        btnSave = view.findViewById(R.id.btnSave)
        btnSave.setOnClickListener { _ ->
            var str = view.addr_edittext?.text.toString()
            str = HtmlCompat.fromHtml(str, HtmlCompat.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL).toString()
            // str = str.replace("\n", "_aa_")
            Toast.makeText(activity, view.addr_edittext?.text.toString(), Toast.LENGTH_LONG).show()
            dLog(str)
        }


        val animalList = arrayOf("Lion", "Tiger", "Monkey", "Elephant", "Dog", "Cat", "Camel")
        val callList = arrayOf("Gọi KH update sau đi xem",
            "Gọi Gọi chủ tin đăng và KH set up Đặt Cọc"
            , "Gọi nhắc nhở KH", "Gọi KH báo hàng mới", "Gọi cho KH khi không có hàng phù hợp")
        // ArrayAdapter.createFromResource(this.activity, animalList, android.R.layout.simple_spinner_item)
        val arrayAdapter = ArrayAdapter<String>(this.activity, android.R.layout.simple_spinner_item, animalList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.material_spinner_2.adapter = arrayAdapter
        view.material_spinner_2.hint = "-- Chon cai gì --"

        view.material_spinner_2.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                Log.v("MaterialSpinner", "onItemSelected parent=${parent.id}, position=$position")
                parent.focusSearch(View.FOCUS_UP)?.requestFocus()
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
                Log.v("MaterialSpinner", "onNothingSelected parent=${parent.id}")
            }
        }


        val adapter = ArrayAdapter<String>(this.activity, android.R.layout.simple_spinner_item, callList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.main_occupation.setAdapter(adapter)
        view.main_occupation.getSpinner().onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(activity, "onItemSelected parent - position=$position", Toast.LENGTH_LONG).show()
            }
        }


        val lstResultCall = arrayListOf(
            SpinnerItem(85, "Gọi KH update sau đi xem"),
            SpinnerItem(91, "Gọi Gọi chủ tin đăng và KH set up Đặt Cọc"),
            SpinnerItem(93, "Gọi nhắc nhở KH"),
            SpinnerItem(96, "Gọi KH báo hàng mới"),
            SpinnerItem(97, "Gọi cho KH khi không có hàng phù hợp")
        )

        val aaa = object : DbHintAdapter<SpinnerItem>(activity!!,  "Kết quả cuộc gọi", lstResultCall) {}
        // aaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinnerResultCall = DbHintSpinner<SpinnerItem>(view.spnOptions, aaa, object : DbHintSpinner.Callback<SpinnerItem> {
            override fun onItemSelected(position: Int, itemAtPosition: SpinnerItem) {
                activity?.toast("position $position ")
            }
        })

        spinnerResultCall.init()

        view.hintablespinner.setOnItemSelectedListener(object : HintableSpinner.OnItemSelectedListener {
            override fun onItemSelected(isNothingSelected: Boolean, view: View?, position: Int, item: String?) {
                if (isNothingSelected) return
                Toast.makeText(activity, String.format("selected %s -> %s", position, item), Toast.LENGTH_SHORT).show()
            }
        })
        view.hintablespinner.addDropdownList("A", "B", "C", "D", "E")


        view.dbspinner.setOnItemSelectedListener(object : DbSpinner.OnItemSelectedListener {
            override fun onItemSelected(isNothingSelected: Boolean, view: View?, position: Int, item: String?) {
                if (isNothingSelected) return
                Toast.makeText(activity, String.format("selected %s -> %s", position, item), Toast.LENGTH_SHORT).show()
            }
        })
        // view.dbspinner.addDropdownList("A", "B", "C", "D", "E")
        view.dbspinner.addAllDropdownItemList(lstResultCall)


//        , object : DbHintSpinner.Callback<SpinnerItem> { //position, itemAtPosition ->
//            override fun onItemSelected(position: Int, itemAtPosition: SpinnerItem) {
//                activity?.toast("position $position ")
//            }
//        })

//        val spinnerResultCall = DbHintSpinner(view.spnOptions, aaa, object : DbHintSpinner.Callback<SpinnerItem> { //position, itemAtPosition ->
//            override fun onItemSelected(position: Int, itemAtPosition: SpinnerItem) {
//                activity?.toast("position $position ")
//            }
//        })
//        spinnerResultCall.init()

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