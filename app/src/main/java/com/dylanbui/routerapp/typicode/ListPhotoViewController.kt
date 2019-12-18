package com.dylanbui.routerapp.typicode

import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.dylanbui.routerapp.MainActivity
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.controller.EndlessRecyclerViewScrollListener
import com.dylanbui.routerapp.utils.guard
import com.dylanbui.routerapp.utils.toast


class ListPhotoViewController : Controller(), PhotoListAdapter.PhotoRowListener {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutRefresh: SwipeRefreshLayout
    var progressView: ViewGroup? = null

    // Save state
    private var recyclerViewState: Parcelable? = null

    // Inject dependencies once per life of Controller
    val inject by lazy { injectDependencies() }

    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private lateinit var photoAdapter: PhotoListAdapter

    private fun setTitle(): String = "Title Photo"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        // Van giu Controler in Memory, khi push new controller
        retainViewMode = RetainViewMode.RETAIN_DETACH

        var view: View = inflater.inflate(R.layout.controller_photo, container, false)
        onViewBound(view)
        return view
    }

    private fun onViewBound(view: View) {
        recyclerView = view.findViewById(R.id.cycView)
        layoutRefresh = view.findViewById(R.id.refreshLayout)
        progressView = view.findViewById(R.id.progressView)

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

        loadData(0)
    }

    fun injectDependencies() {

        Log.d("RUN ONCE", "injectDependencies")
    }


    override fun onAttach(view: View)
    {
        inject // Chi chay 1 lan trong controller lifecycle

        super.onAttach(view)

        var mainActivity = activity as? MainActivity

        mainActivity?.let {
            it.setToolBarTitle(setTitle())
            it.enableUpArrow(router.backstackSize > 1)
        }


//        recyclerViewState?.let {
//            // recyclerView.layoutManager?.onRestoreInstanceState(it)
//            progressView?.visibility = View.GONE
//            return
//        }

        Log.d("TAG", "onAttach")
        //loadData(0)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        Log.d("TAG", "onDetach")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "onDestroy onDestroy")
    }

    fun loadData(page: Int) {

        TyPhotoApi.getPhoto(page) { list, appNetworkServiceError ->

            appNetworkServiceError?.let {
                activity?.toast(it.errorMessage)
                // have error
                return@getPhoto
            }
            // Reload data
            if (page == 0) photoAdapter.clearData()
            photoAdapter.updateData(list.toMutableList())
            layoutRefresh.isRefreshing = false
            progressView?.visibility = View.GONE
        }

    }

    override fun onRowClick(position: Int, photo: TyPhoto) {
        //activity?.toast(photo.title ?: "Khong co")
        //recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState() //save

        var vcl = DetailPhotoViewController()
        vcl.tyPhoto = photo

        router.pushController(
            RouterTransaction.with(vcl)
                .pushChangeHandler(HorizontalChangeHandler())
                .popChangeHandler(HorizontalChangeHandler()))

    }



}

