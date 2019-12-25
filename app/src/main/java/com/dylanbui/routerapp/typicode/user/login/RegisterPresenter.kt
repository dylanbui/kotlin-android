package com.dylanbui.routerapp.typicode.user.login

import com.dylanbui.routerapp.BaseMvpPresenter
import com.dylanbui.routerapp.BaseMvpView
import com.dylanbui.routerapp.networking.AppNetworkServiceError


interface RegisterActionView : BaseMvpView
{
    fun registerComplete()
    fun showLoginError(error : AppNetworkServiceError)
}


class RegisterPresenter : BaseMvpPresenter<RegisterActionView>()
{
    fun doRegister(username: String, password: String)
    {

    }

}