package com.dylanbui.routerapp.typicode.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.networking.AppNetworkServiceError
import com.dylanbui.routerapp.typicode.TyPost
import com.dylanbui.android_library.utils.toast

class PostDetailViewController: BaseMvpController<PostDetailActionView, PostDetailPresenter>(), PostDetailActionView {

    lateinit var tyPost: TyPost

    lateinit var txtId: TextView
    lateinit var txtTitle: TextView
    lateinit var txtBody: TextView

    override fun createPresenter(): PostDetailPresenter = PostDetailPresenter()

    override fun setTitle(): String? = "Detail Post Id: ${tyPost.id.toString()}"

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_post_detail, container, false)
    }

    override fun onViewBound(view: View) {
        txtId = view.findViewById<TextView>(R.id.txtId)
        txtTitle = view.findViewById<TextView>(R.id.txtTitle)
        txtBody = view.findViewById<TextView>(R.id.txtBody)
    }

    override fun onPreAttach() {
        // -- Load data --
        presenter.getPostDetail(tyPost.id!!)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
    }

    override fun updatePostDetail(post: TyPost) {
        txtId.text = "ID: ${post.id.toString()}"
        txtTitle.text = "Title: ${post.title.toString()}"
        txtBody.text = "Body: ${post.body.toString()}"
        progressView?.visibility = View.GONE
    }

    override fun showPostError(error: AppNetworkServiceError) {
        activity?.toast(error.errorMessage)
    }

}