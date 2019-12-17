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

class ListUserViewController : Controller(), UserListAdapter.UserRowListener
{
    lateinit var recyclerView: RecyclerView
    lateinit var layoutRefresh: SwipeRefreshLayout

    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private lateinit var userAdapter: UserListAdapter

    private fun setTitle(): String = "Title User"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View
    {
        var view: View = inflater.inflate(R.layout.controller_user, container, false)
        onViewBound(view)
        return view
    }

    private fun onViewBound(view: View)
    {
        recyclerView = view.findViewById(R.id.cycView)
        layoutRefresh = view.findViewById(R.id.refreshLayout)

        userAdapter = UserListAdapter(view.context, this)

        var layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = userAdapter

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // presenter.getUserList(page, false)
            }
        }
        recyclerView.addOnScrollListener(scrollListener!!)

        layoutRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                //surveyAdapter.clearData()
                // presenter.getUserList(1, false)
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

        TyUserApi.getUser { list, appNetworkServiceError ->

            appNetworkServiceError?.let {
                activity?.toast(it.errorMessage)
                // have error
                return@getUser
            }
            // Reload data
            userAdapter.clearData()
            userAdapter.updateData(list.toMutableList())
        }





    }

    override fun onRowClick(position: Int, user: TyUser) {
        activity?.toast(user.name ?: "Khong co")
    }



}

