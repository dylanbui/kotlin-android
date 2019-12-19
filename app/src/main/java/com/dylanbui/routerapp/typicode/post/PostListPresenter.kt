package com.dylanbui.routerapp.typicode.post

import com.dylanbui.routerapp.BaseMvpPresenter
import com.dylanbui.routerapp.BaseMvpView
import com.dylanbui.routerapp.networking.AppNetworkServiceError
import com.dylanbui.routerapp.typicode.TyPost
import com.dylanbui.routerapp.typicode.TyPostApi

interface PostListActionView : BaseMvpView
{
    fun onPostRowClick(position: Int, post: TyPost)
    fun updatePostList(page: Int, list: List<TyPost>)
    fun showPostError(error : AppNetworkServiceError)
//    fun setData(list: List<User>)
//    fun showContent()

//    fun updateListSurvey(page:Int,userList: List<User>)
}


class PostListPresenter : BaseMvpPresenter<PostListActionView>()
{
    fun getPostList(page: Int = 0) {
        TyPostApi.getPost { list, appNetworkServiceError ->
            appNetworkServiceError?.let {
                // activity?.toast(it.errorMessage)
                // have error
                return@getPost
            }
            // Reload data
            ifViewAttached { it.updatePostList(page, list) }
        }
    }

    fun onPostRowClick(position: Int, post: TyPost) {
        // -- Code xu ly khi click vao row --
        ifViewAttached { view -> view.onPostRowClick(position, post) }
    }


}