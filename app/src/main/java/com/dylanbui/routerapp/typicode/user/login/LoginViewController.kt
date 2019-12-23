package com.dylanbui.routerapp.typicode.user.login

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.controller.EndlessRecyclerViewScrollListener
import com.dylanbui.routerapp.networking.AppNetworkServiceError
import com.dylanbui.routerapp.utils.EditTextUtils
import com.dylanbui.routerapp.utils.Utils


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
            this.doLogin()
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

