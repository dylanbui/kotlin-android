package com.dylanbui.routerapp.typicode.install_apk

import com.dylanbui.routerapp.BaseMvpPresenter
import com.dylanbui.routerapp.BaseMvpView


interface InstallApkActionView : BaseMvpView
{
//    fun updatePostDetail(post: TyPost)
//    fun showPostError(error : AppNetworkServiceError)
}


class InstallApkPresenter : BaseMvpPresenter<InstallApkActionView>()
{
//    fun getPostDetail(postId: Int) {
//        TyPostApi.getPostDetail(postId) { post, appNetworkServiceError ->
//            appNetworkServiceError?.let {
//                // have error
//                ifViewAttached { it.showPostError(appNetworkServiceError) }
//                return@getPostDetail
//            }
//            // Reload data
//            ifViewAttached { it.updatePostDetail(post) }
//        }
//    }
}