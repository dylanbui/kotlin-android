package com.dylanbui.routerapp.controller.user

import com.dylanbui.routerapp.BaseMvpPresenter
import com.dylanbui.routerapp.BaseMvpView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

interface UserActionView : BaseMvpView
{
    fun onUserRowClick(user: User)
    fun showUserList(list: List<User>)
    fun setData(list: List<User>)
    fun showContent()
    fun showError(error : Throwable, pullToRefresh: Boolean)
    fun updateListSurvey(page:Int,userList: List<User>)
}


class UserPresenter : BaseMvpPresenter<UserActionView>()
//class UserPresenter : MvpBasePresenter<UserActionView>()
{
    // private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getUserList(page: Int, isLoading: Boolean) {
        compositeDisposable.add(getNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.map { User(it.id, it.name, it.email) } }
            .subscribe({
                ifViewAttached { view ->
                    view.updateListSurvey(page, it)
                }
            }, { error -> ifViewAttached {   } }))
    }

    fun loadUser(pullToRefresh: Boolean)
    {
//        compositeDisposable.add(getHelloGreeting()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { ifViewAttached { view -> view.showLoading() } }
//            .doFinally { ifViewAttached { view -> view.hideLoading() } }
//            .subscribe({ ifViewAttached { view -> view.showUser(it) } }, { ifViewAttached { view -> view.showError() } }))

        compositeDisposable.add(getNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.map { User(it.id, it.name, it.email) } }
            .subscribe({
                ifViewAttached { view ->
                    view.setData(it)
                    view.showContent()
                }
            }, { error -> ifViewAttached { view -> view.showError(error, pullToRefresh) } }))

    }

    fun showUserError()
    {
        ifViewAttached { view ->
            // view.showError()
        }
    }

    private fun getNotes(): Single<List<User>> = Single.just(generateNotes()).delay(2, TimeUnit.SECONDS)

    private fun generateNotes() = (1..(2..50).random()).map { User(it, "note $it", "email_$it@yahoo.com" ) }

    private fun getHelloGreeting(): Single<String>
    {
        return Single.just("hi there!").delay(2, TimeUnit.SECONDS)
    }
}