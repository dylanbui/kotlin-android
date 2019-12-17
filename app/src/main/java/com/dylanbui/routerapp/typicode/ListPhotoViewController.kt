package com.dylanbui.routerapp.typicode

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bluelinelabs.conductor.Controller
import com.dylanbui.routerapp.MainActivity
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.controller.EndlessRecyclerViewScrollListener
import com.dylanbui.routerapp.utils.toast

class ListPhotoViewController : Controller(), PhotoListAdapter.PhotoRowListener
{
    lateinit var recyclerView: RecyclerView
    lateinit var layoutRefresh: SwipeRefreshLayout

    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private lateinit var photoAdapter: PhotoListAdapter

    private fun setTitle(): String = "Title Photo"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View
    {
        var view: View = inflater.inflate(R.layout.controller_photo, container, false)
        onViewBound(view)
        return view
    }

    private fun onViewBound(view: View)
    {
        recyclerView = view.findViewById(R.id.cycView)
        layoutRefresh = view.findViewById(R.id.refreshLayout)

        photoAdapter = PhotoListAdapter(view.context, this)

        var layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = photoAdapter

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // presenter.getUserList(page, false)
                loadData(page)
            }
        }
        recyclerView.addOnScrollListener(scrollListener!!)

        layoutRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                //surveyAdapter.clearData()
                // presenter.getUserList(1, false)
                loadData(0)
            }
        })

    }
    override fun onAttach(view: View)
    {
        super.onAttach(view)

        var mainActivity = activity as? MainActivity

        mainActivity?.let {
            it.setToolBarTitle(setTitle())
            it.enableUpArrow(router.backstackSize > 1)
        }

        loadData(0)
    }

    fun loadData(page: Int) {

        TyPhotoApi.getPhoto(page) { list, appNetworkServiceError ->

            appNetworkServiceError?.let {
                activity?.toast(it.errorMessage)
                // have error
                return@getPhoto
            }
            // Reload data
            photoAdapter.clearData()
            photoAdapter.updateData(list.toMutableList())
        }

    }

    override fun onRowClick(position: Int, user: TyPhoto) {
        // activity?.toast(user.name ?: "Khong co")
    }



}

