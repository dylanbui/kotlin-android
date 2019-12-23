package com.dylanbui.routerapp

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

open class DisposableImpl<T>: DisposableObserver<T>()
{
    override fun onComplete() {
    }

    override fun onNext(t: T) {
    }

    override fun onError(e: Throwable) {
    }
}

class ForceLogoutEvent(var message:String)
{

}

interface BaseMvpView : MvpView
{
    fun getStringResource(resourceString: Int): String
    fun showLoading()
    fun hideLoading()
}

// dispatches execution into Android main thread
val uiDispatcher: CoroutineDispatcher = Dispatchers.Main
// represent a pool of shared threads as coroutine dispatcher
val bgDispatcher: CoroutineDispatcher = Dispatchers.IO

abstract class BaseMvpPresenter<V : BaseMvpView> : MvpBasePresenter<V>()
{
    protected lateinit var compositeDisposable: CompositeDisposable
    var exitOnBackPressAgain = false

    fun setExitAppOnBackPress()
    {
        exitOnBackPressAgain = true
        compositeDisposable.add(
            Observable.just(1L)
                .delay(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableImpl<Long>() {
                    override fun onNext(t: Long) {
                        exitOnBackPressAgain = false
                    }
                }))
    }

    override fun attachView(view: V)
    {
        super.attachView(view)
        EventBus.getDefault().register(this)
        compositeDisposable = CompositeDisposable()
    }

    fun addSubscription(disposable: Disposable) = compositeDisposable.add(disposable)
    fun removeSubscription(disposable: Disposable) = compositeDisposable.remove(disposable)

    override fun detachView()
    {
        compositeDisposable.clear()
        EventBus.getDefault().unregister(this)
        super.detachView()
    }

    override fun destroy()
    {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onForceLogoutEvent(forceLogoutEvent: ForceLogoutEvent)
    {
        ifViewAttached { v ->
//            v.showWarningMessage(forceLogoutEvent.message, R.string.login, object : CallbackNullResponse() {
//                override fun onResponse() {
//                    try {
//                        addSubscription(
//                            Observable.just(1L).delay(300, TimeUnit.MILLISECONDS)
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribeWith(object : DisposableImpl<Long>() {
//                                    override fun onNext(t: Long) {
//                                        PreUtil.remove(PreUtil.KEY_USER)
//                                        SurveyApp.user = null
//                                        PreUtil.remove(PreUtil.KEY_PROFILE)
//                                        SurveyApp.profile = null
//                                        v.forceLogout()
//                                    }
//                                }))
//                        var logoutRequest = LogoutRequest(SurveyApp.deviceToken!!)
//                        addSubscription(
//                            UserService.logout(logoutRequest)
//                                .subscribeWith(object : DisposableImpl<Empty>() {
//                                    override fun onNext(t: Empty) {
//                                        PreUtil.remove(PreUtil.KEY_USER)
//                                        SurveyApp.user = null
//                                        PreUtil.remove(PreUtil.KEY_PROFILE)
//                                        SurveyApp.profile = null
//                                    }
//
//                                    override fun onError(e: Throwable) {
//                                        PreUtil.remove(PreUtil.KEY_USER)
//                                        SurveyApp.user = null
//                                        PreUtil.remove(PreUtil.KEY_PROFILE)
//                                        SurveyApp.profile = null
//                                    }
//                                }))
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            })
        }
    }

//    override fun getStringResource(resourceString: Int): String
//    {
//        return "nothing"
//    }
//
//    override fun showLoading()
//    {
//
//    }
//
//    override fun hideLoading()
//    {
//
//    }
}