package com.dylanbui.routerapp.retrofit

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
import com.dylanbui.android_library.utils.fromJson
import com.dylanbui.routerapp.networking.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import java.util.*


class PostViewController : Controller(), PostsAdapter.PostRowListener
{
    lateinit var recyclerView: RecyclerView
    lateinit var layoutRefresh: SwipeRefreshLayout

    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private lateinit var postAdapter: PostsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View
    {
        var view: View = inflater.inflate(R.layout.controller_post, container, false)
        onViewBound(view)
        return view
    }


    private fun setTitle(): String = "Title Posts"

    private fun onViewBound(view: View)
    {
        recyclerView = view.findViewById(R.id.cycView)
        layoutRefresh = view.findViewById(R.id.refreshLayout)

        postAdapter = PostsAdapter(view.context, this)

        var layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = postAdapter

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

        // Chay dung
//        RetrofitFactory.makeRetrofitService().create(PostService::class.java).getCallPosts().enqueue(object: Callback<List<Post>> {
//            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
//                if (response.code() == 200) {
//                    response.body()?.let {
//                        postAdapter.clearData()
//                        postAdapter.updateData(it.toMutableList())
//                    }
//                }
//            }
//            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
//
//            }
//        })

//        RetrofitFactory.makeRetrofitService().create(PostService::class.java).getCallPosts().enqueue(object: Callback<List<Post>> {
//            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
//                if (response.code() == 200) {
//                    response.body()?.let {
//                        postAdapter.clearData()
//                        postAdapter.updateData(it.toMutableList())
//                    }
//                }
//            }
//            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
//
//            }
//        })

//        class Et: ServiceCallBack<List<District>> {
//            override fun onSuccess(result: List<District>?, responseBody: CloudResponse) {
//                result?.let {
//                    it.forEach {
//                        println("Price of book, ${it.districtId} is ${it.districtName}")
//                    }
////                    postAdapter.clearData()
////                    postAdapter.updateData(it.toMutableList())
//                }
//            }
//
//            override fun onFailed(errorData: MyNetworkingServiceError) {
//                println("loi me no roi")
//            }
//
//        }

        AppNetwork.setBaseUrl("http://45.117.162.60:8080/diy/api/")

        AppNetwork.review()

        //MyNetworking.getInstance().request("/posts", Et())
        // MyNetworking.getInstance().request("districts/-1", Et())
        MyNetworking.getInstance().request("districts/-1",
            onResponse = { responseBody: CloudResponse ->

                responseBody.data?.let {
                    val myList: List<District> =
                        fromJson(it)
                    myList.forEach { item ->
                        println("Price of book, ${item.districtId} is ${item.districtName}")
                    }
                    print("Xong hang")
                }

        }, onFailed = {errorData: AppNetworkServiceError ->
                println(errorData)
        })


//        RetrofitFactory.makeRetrofitService().create(PostService::class.java).getResponseBody().enqueue(
//            object: Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
////                if (response.code() == 200) {
////                    response.body()?.let {
////                        // response.body() == it ,  is string json
////                    }
////                }
//            }
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//
//            }
//        })



        val jsonObject = JsonObject()
        jsonObject.addProperty("title", "Hello")
        jsonObject.addProperty("body", "Retrofit")
        jsonObject.addProperty("userId", 2)
        jsonObject.addProperty("none", "null")

        var valHash = HashMap<String, Any?>()
        // valHash["title"] = "ten tao"
//        valHash.put("name", "ten gi")
//        valHash.put("title", null)
        valHash["ten"] = "hoi chi"
        valHash["ngay"] = 12


        val bui = MultipartBody.Builder() // is RequestBody
        bui.addFormDataPart("ten", "hai ghe")
        bui.addFormDataPart("so", 123.23.toString())
        // bui.addFormDataPart("gia", null)


    }

    override fun onRowClick(position: Int, user: Post) {

    }

//    override fun updateListSurvey(page: Int, userList: List<User>) {
//        if (page == 1) {
//            usersAdapter.clearData()
//            // emptyData(surveyList.size == 0)
//        } else {
//            // emptyData(false)
//        }
//        usersAdapter.updateData(userList.toMutableList())
//        layoutRefresh.isRefreshing = false
//    }


}

