package com.dylanbui.routerapp.typicode.user.login

import android.text.TextUtils
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.networking.AppNetworkServiceError
import com.dylanbui.routerapp.utils.EditTextUtils
import com.dylanbui.routerapp.utils.dLog
import com.dylanbui.routerapp.utils.defaultPushController
import com.dylanbui.routerapp.utils.toast



class RegisterViewController : BaseMvpController<RegisterActionView, RegisterPresenter>(), RegisterActionView {

    lateinit var txtUsername: EditText
    lateinit var txtPassword: EditText

    override fun setTitle(): String? = "Register Account"

    override fun createPresenter(): RegisterPresenter = RegisterPresenter()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_register, container, false)
    }

    override fun onViewBound(view: View) {
        txtUsername = view.findViewById(R.id.txtUsername)
        txtPassword = view.findViewById(R.id.txtPassword)

        // -- At here presenter == null --
        var btnRegister = view.findViewById<Button>(R.id.btnRegister)
        btnRegister.setOnClickListener {
            // this.doLogin()
            router.defaultPushController(ForgotPwViewController())
        }

        var btnDialog = view.findViewById<Button>(R.id.btnDialog)
        btnDialog.setOnClickListener {
            router.pushController(
                RouterTransaction.with(DemoDialogView())
                    .pushChangeHandler(FadeChangeHandler(false))
                    .popChangeHandler(FadeChangeHandler()))
        }

        var btnModal = view.findViewById<Button>(R.id.btnModal)
        btnModal.setOnClickListener {

        }

        // setHasOptionsMenu(true)

//        toolbar_login = view.findViewById(R.id.toolbar)
//        // toolbar_login?.visibility = View.GONE // Hide toolbar
//        toolbar_login?.title = "Register Account"
//        this.enableUpArrow()

//        toolbar_login?.setNavigationIcon(R.drawable.ic_arrow_back)
//        // need to set the icon here to have a navigation icon. You can simple create an vector image by "Vector Asset" and using here
//        toolbar_login?.setNavigationOnClickListener {
//            // do something when click navigation
//        }

        toolbar?.menu?.clear() // Remove all item in menu
        var menu = toolbar?.menu
        menu?.let {
            it.add(Menu.NONE, 11, 0, "View RSS")
                .setIcon(R.mipmap.ic_delete)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS) // .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

            it.add(Menu.NONE, 22, 1, "Read RSS")
                .setIcon(R.mipmap.ic_error)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS) // .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }

        var menuItem = menu?.findItem(11)
        menuItem?.isVisible = false // Hide menu item

        // toolbar_login?.inflateMenu(R.menu.menu_login)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                11 -> {
                    activity?.toast("View RSS")
                    true
                }
                22 -> {
                    activity?.toast("Read RSS")
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }



    }

    override fun onPreAttach() {
        // -- At here presenter is existed --
        // -- Get action in row => presenter --
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        activity?.let {
            val controllerContainer = view.findViewById<ViewGroup>(R.id.controllerContainer)
            EditTextUtils.setupEditTextUI(controllerContainer, it)
            // EditTextUtils.requestFocus(txtUsername, it)
        }

    }


    private fun doLogin() {
        if (TextUtils.isEmpty(txtUsername.text)) {
            // showWarningMessage(getStringResource(R.string.invalid_phone), R.string.close, null)
            return
        }
        if (TextUtils.isEmpty(txtPassword.text)) {
            // showWarningMessage(getStringResource(R.string.invalid_password), R.string.close, null)
            return
        }
        // presenter.doLogin(txtUsername.text.toString(), txtPassword.text.toString())
    }

    /**
     * RegisterActionView interface
     */

    override fun registerComplete() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showLoginError(error: AppNetworkServiceError) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

