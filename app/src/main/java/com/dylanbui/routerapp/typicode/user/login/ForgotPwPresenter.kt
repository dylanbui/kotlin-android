package com.dylanbui.routerapp.typicode.user.login

import com.dylanbui.routerapp.BaseMvpPresenter
import com.dylanbui.routerapp.BaseMvpView
import com.dylanbui.routerapp.networking.AppNetworkServiceError


interface ForgotPwActionView : BaseMvpView
{
    fun changeComplete()
    fun showLoginError(error : AppNetworkServiceError)
}


class ForgotPwPresenter : BaseMvpPresenter<ForgotPwActionView>()
{
    fun doChange(username: String, password: String)
    {

    }

}