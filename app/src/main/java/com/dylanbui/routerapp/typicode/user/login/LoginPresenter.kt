package com.dylanbui.routerapp.typicode.user.login

import com.dylanbui.routerapp.BaseMvpPresenter
import com.dylanbui.routerapp.BaseMvpView
import com.dylanbui.routerapp.networking.AppNetworkServiceError


interface LoginActionView : BaseMvpView
{
    fun loginComplete()
    fun showLoginError(error : AppNetworkServiceError)
}


class LoginPresenter : BaseMvpPresenter<LoginActionView>()
{
    fun doLogin(username: String, password: String)
    {

    }

}