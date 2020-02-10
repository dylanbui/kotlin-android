package com.dylanbui.routerapp.typicode.bottom_sheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment
import com.commit451.modalbottomsheetdialogfragment.Option
import com.commit451.modalbottomsheetdialogfragment.OptionRequest
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R

class DemoBottomSheetViewController :
    BaseMvpController<DemoBottomSheetViewAction, DemoBottomSheetPresenter>(),
    DemoBottomSheetViewAction,
    ModalBottomSheetDialogFragment.Listener {

    override fun setTitle(): String? = "DemoBottomSheet"

    override fun createPresenter(): DemoBottomSheetPresenter = DemoBottomSheetPresenter()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_demo_bottom_sheet, container, false)
    }

    override fun onViewBound(view: View) {


        val btnShowSheet = view.findViewById<Button>(R.id.btnShowSheet)
        btnShowSheet.setOnClickListener {
             val sheet = MySheetDialogFragment()
            //mainActivity.supportFragmentManager
            val act = activity as AppCompatActivity
             sheet.show(act.supportFragmentManager, "DemoBottomSheetFragment")
            // modal.show(activity!!.supportFragmentManager, "DemoBottomSheetFragment")
        }

    }

    override fun onPreAttach() {
        // -- At here presenter is existed --
        // -- Get action in row => presenter --
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
    }


    override fun onModalOptionSelected(tag: String?, option: Option) {
    }
}

