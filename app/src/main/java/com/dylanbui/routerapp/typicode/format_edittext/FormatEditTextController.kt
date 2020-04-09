package com.dylanbui.routerapp.typicode.format_edittext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dylanbui.android_library.ui_control.decimal_edittext.DbAutoFormatEditText
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R
import kotlinx.android.synthetic.main.controller_format_edit_text.view.*

class FormatEditTextController : BaseMvpController<FormatEditTextViewAction, FormatEditTextPresenter>(), FormatEditTextViewAction {

    override fun setTitle(): String? = "FormatEditText"

    override fun createPresenter(): FormatEditTextPresenter = FormatEditTextPresenter()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_format_edit_text, container, false)
    }

    override fun onViewBound(view: View) {

    }

    override fun onPreAttach() {
        // -- At here presenter is existed --
        // -- Get action in row => presenter --
    }

    override fun onAttach(view: View) {
        super.onAttach(view)


        val decimal: DbAutoFormatEditText = view?.edtDecimal
        decimal.setValue("123456789123456")

        val nonDecimal: DbAutoFormatEditText = view?.edtNonDecimal
        nonDecimal.setValue(9876543210.0)

    }


}

