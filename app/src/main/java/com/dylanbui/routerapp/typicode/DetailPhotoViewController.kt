package com.dylanbui.routerapp.typicode

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bluelinelabs.conductor.Controller
import com.dylanbui.routerapp.MainActivity
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.controller.EndlessRecyclerViewScrollListener
import com.dylanbui.routerapp.utils.toast
import com.squareup.picasso.Picasso

class DetailPhotoViewController : Controller()
{
    lateinit var tyPhoto: TyPhoto // Co y truyen bien vao

    lateinit var imageView: ImageView
    lateinit var txtTitle: TextView
    lateinit var txtUrl: TextView
    var progressView: ViewGroup? = null


    private fun setTitle(): String = "Detail Photo"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View
    {
        var view: View = inflater.inflate(R.layout.controller_photo_detail, container, false)
        onViewBound(view)
        return view
    }

    private fun onViewBound(view: View)
    {
        imageView = view.findViewById(R.id.imageView)
        txtTitle = view.findViewById(R.id.txtTitle)
        txtUrl = view.findViewById(R.id.txtUrl)
        progressView = view.findViewById(R.id.progressView)

    }
    override fun onAttach(view: View)
    {
        super.onAttach(view)

        var mainActivity = activity as? MainActivity

        mainActivity?.let {
            it.setToolBarTitle(setTitle())
            it.enableUpArrow(router.backstackSize > 1)
        }

        loadData()
    }

    fun loadData() {

        TyPhotoApi.getDetailPhoto(tyPhoto.id!!) { photo, appNetworkServiceError ->

            appNetworkServiceError?.let {
                activity?.toast(it.errorMessage)
                // have error
                return@getDetailPhoto
            }

            // Reload data
            txtTitle.text = photo.title
            txtUrl.text = photo.url
            Picasso.get().load(photo.url).into(imageView)
            progressView?.visibility = View.GONE
        }

    }



}

