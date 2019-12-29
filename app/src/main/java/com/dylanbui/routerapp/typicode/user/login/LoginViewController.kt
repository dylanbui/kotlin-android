package com.dylanbui.routerapp.typicode.user.login

import android.text.TextUtils
import android.view.*
import android.widget.Button
import android.widget.EditText
import com.dylanbui.android_library.utils.EditTextUtils
import com.dylanbui.android_library.utils.dLog
import com.dylanbui.android_library.utils.toast
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.networking.AppNetworkServiceError
import com.dylanbui.routerapp.utils.defaultPushController


class LoginViewController : BaseMvpController<LoginActionView, LoginPresenter>(), LoginActionView {


    lateinit var txtUsername: EditText
    lateinit var txtPassword: EditText

    override fun setTitle(): String? = "Login"

    override fun createPresenter(): LoginPresenter = LoginPresenter()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_login, container, false)
    }

    override fun onViewBound(view: View) {
        txtUsername = view.findViewById(R.id.txtUsername)
        txtPassword = view.findViewById(R.id.txtPassword)

        // -- At here presenter == null --
        var btnLogin = view.findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            router.defaultPushController(RegisterViewController())
            //this.doLogin()
        }

        // setHasOptionsMenu(true)

//        toolbar_login?.setNavigationIcon(R.drawable.ic_arrow_back)
        // need to set the icon here to have a navigation icon. You can simple create an vector image by "Vector Asset" and using here
//        toolbar_login?.setNavigationOnClickListener {
//            // do something when click navigation
//        }

        var menu = toolbar?.menu
        menu?.let {
            it.add(Menu.NONE, 11, 0, "View RSS")
                .setIcon(R.mipmap.ic_delete)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER) // .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

            it.add(Menu.NONE, 22, 1, "Read RSS")
                .setIcon(R.mipmap.ic_error)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER) // .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }


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
                R.id.action_cut -> {
                    activity?.toast("action_cut")
                    true
                }
                R.id.action_copy -> {
                    activity?.toast("action_copy")
                    true
                }
                R.id.action_add -> {
                    dLog("R.id.action_add")
                    // do something
                    true
                }
                R.id.action_update_room -> {
                    dLog("R.id.action_update_room")
                    // do something
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
            EditTextUtils.requestFocus(txtUsername, it)
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
        presenter.doLogin(txtUsername.text.toString(), txtPassword.text.toString())
    }

    /**
     * LoginActionView interface
     */

    override fun loginComplete() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoginError(error: AppNetworkServiceError) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

