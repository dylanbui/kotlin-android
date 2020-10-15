package com.dylanbui.android_library.mvp_structure

import com.dylanbui.android_library.DbError
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

open class DisposableImpl<T> : DisposableObserver<T>() {
    override fun onComplete() {
    }

    override fun onNext(t: T) {
    }

    override fun onError(e: Throwable) {
    }
}

class ForceLogoutEvent(var message: String) {

}

interface DbBaseMvpView : MvpView {
    fun getStringResource(resourceId: Int): String
    fun getStringQuantity(resourceId: Int, numberCount: Int, vararg text: String): String { return "" }
    fun showLoading()
    fun hideLoading()
    fun showToast(text: String)
    fun showError(error: DbError)
    fun showProgressView()
    fun hideProgressView()
}

// dispatches execution into Android main thread
val uiDispatcher: CoroutineDispatcher = Dispatchers.Main

// represent a pool of shared threads as coroutine dispatcher
val bgDispatcher: CoroutineDispatcher = Dispatchers.IO

val cpuDispatcher: CoroutineDispatcher = Dispatchers.Default

abstract class DbBaseMvpPresenter<V : DbBaseMvpView> : MvpBasePresenter<V>(), CoroutineScope {
    protected lateinit var compositeDisposable: CompositeDisposable
    var exitOnBackPressAgain = false
    var nav: DbNavigation? = null

    // This launch uses the coroutineContext defined
    // by the coroutine presenter.
    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    fun setExitAppOnBackPress() {
        exitOnBackPressAgain = true
        compositeDisposable.add(
            Observable.just(1L)
                .delay(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableImpl<Long>() {
                    override fun onNext(t: Long) {
                        exitOnBackPressAgain = false
                    }
                })
        )
    }

    override fun attachView(view: V) {
        super.attachView(view)
        EventBus.getDefault().register(this)
        compositeDisposable = CompositeDisposable()
    }

    fun addSubscription(disposable: Disposable) = compositeDisposable.add(disposable)
    fun removeSubscription(disposable: Disposable) = compositeDisposable.remove(disposable)

//    override fun detachView()
//    {
//        compositeDisposable.clear()
//        EventBus.getDefault().unregister(this)
//        super.detachView()
//    }

    override fun detachView() {
        // By default, every coroutine initiated in this context
        // will use the job and dispatcher specified by the
        // coroutineContext.
        // The coroutines are scoped to their execution environment.
        job.cancel()

        compositeDisposable.clear()
        EventBus.getDefault().unregister(this)

        super.detachView()
    }

    override fun destroy() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onForceLogoutEvent(forceLogoutEvent: ForceLogoutEvent) {
        ifViewAttached { v ->

        }
    }

   open fun isDisplayError(error: DbError?): Boolean {
        ifViewAttached { it.hideProgressView() }
        if (error != null) {
            ifViewAttached { it.showError(error) }
            return true
        }
        return false
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