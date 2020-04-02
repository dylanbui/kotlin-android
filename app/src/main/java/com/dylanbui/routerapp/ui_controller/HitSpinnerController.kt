package com.dylanbui.routerapp.ui_controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dylanbui.android_library.ui_control.hint_spinner.DbHintAdapter
import com.dylanbui.android_library.ui_control.hint_spinner.DbHintSpinner
import com.dylanbui.android_library.utils.onClick
import com.dylanbui.android_library.utils.toast
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R
import kotlinx.android.synthetic.main.controller_hit_spinner.view.*
import kotlinx.android.synthetic.main.item_spinner_base_view.view.*

data class SpinnerItem(val id: Int, val value: String)

class HitSpinnerController : BaseMvpController<HitSpinnerViewAction, HitSpinnerPresenter>(),
    HitSpinnerViewAction {

    lateinit var spinnerResultCall: DbHintSpinner<SpinnerItem>
    lateinit var lstResultCall: ArrayList<SpinnerItem>

    override fun setTitle(): String? = "HitSpinner"
    override fun createPresenter(): HitSpinnerPresenter = HitSpinnerPresenter()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_hit_spinner, container, false)
    }

    override fun onViewBound(view: View) {

        view.btnTemplate?.onClick {
            activity?.toast("btnTemplate click")
        }

        lstResultCall = arrayListOf(
            SpinnerItem(85, "Gọi KH update sau đi xem"),
            SpinnerItem(91, "Gọi Gọi chủ tin đăng và KH set up Đặt Cọc"),
            SpinnerItem(93, "Gọi nhắc nhở KH"),
            SpinnerItem(96, "Gọi KH báo hàng mới"),
            SpinnerItem(97, "Gọi cho KH khi không có hàng phù hợp")
        )

        val aaa = object : DbHintAdapter<SpinnerItem>(
            activity!!,  "Kết quả cuộc gọi",
            lstResultCall) {}

        spinnerResultCall = DbHintSpinner(view.spnOptions, aaa, object : DbHintSpinner.Callback<SpinnerItem> { //position, itemAtPosition ->
            override fun onItemSelected(position: Int, itemAtPosition: SpinnerItem) {
                activity?.toast("position $position ")
            }
        })


//        spinnerResultCall = DbHintSpinner(view.spnOptions, object: DbHintAdapter<SpinnerItem>(
//                activity!!, R.layout.item_spinner_base_view, "Kết quả cuộc gọi", lstResultCall) {
//            override fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
//                var type: SpinnerItem = getItem(position)
//                val view = inflateLayout(parent, false)
//                view.title.text = type.value
//                return view
//            }
//        }, object : DbHintSpinner.Callback<SpinnerItem> { //position, itemAtPosition ->
//            override fun onItemSelected(position: Int, itemAtPosition: SpinnerItem) {
//
//            }
//        })
        spinnerResultCall.init()
    }

    override fun onPreAttach() {
        // -- At here presenter is existed --
        // -- Get action in row => presenter --
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
    }


}

