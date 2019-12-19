package com.dylanbui.routerapp.typicode.post

import com.dylanbui.routerapp.BaseMvpPresenter
import com.dylanbui.routerapp.BaseMvpView
import com.dylanbui.routerapp.networking.AppNetworkServiceError
import com.dylanbui.routerapp.typicode.TyPost
import com.dylanbui.routerapp.typicode.TyPostApi

interface PostDetailActionView : BaseMvpView
{
    fun updatePostDetail(post: TyPost)
    fun showPostError(error : AppNetworkServiceError)
}


class PostDetailPresenter : BaseMvpPresenter<PostDetailActionView>()
{
    fun getPostDetail(postId: Int) {
        TyPostApi.getPostDetail(postId) { post, appNetworkServiceError ->
            appNetworkServiceError?.let {
                // have error
                ifViewAttached { it.showPostError(appNetworkServiceError) }
                return@getPostDetail
            }
            // Reload data
            ifViewAttached { it.updatePostDetail(post) }
        }
    }
}