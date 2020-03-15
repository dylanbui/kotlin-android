package com.dylanbui.android_library.utils_adapter

import android.app.Activity
import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

interface OnAdapterListener<in T> {

    fun onSelectedItemListener(model: T, index: Int, view: View? = null)

    fun onSelectedItemLongClickListener(model: T, index: Int, view: View? = null)

    fun onBottomReachedListener(model: T, index: Int)

}


/**
 * Created by Khoa Nguyen on 11/8/17.
 * Copyright (c) 2017. All rights reserved.
 * Email: khoantt91@gmail.com
 */
abstract class BaseRecyclerAdapter<T>(val activity:Context, var listener: OnAdapterListener<T>):RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder>() {

    //region Properties
    var list: List<T> = ArrayList<T>()

//    val itemCount:Int
//        get() {
//            return list.size
//        }


    protected abstract fun createView(context: Context, viewGroup: ViewGroup, viewType: Int) : BaseViewHolder
    protected abstract fun bindView(item: T, position: Int, baseViewHolder: BaseViewHolder)


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int):BaseViewHolder {
        return createView(activity, viewGroup, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position:Int) {
        getItem(position)?.let {
            bindView(it, position, holder)
        }
    }

    fun getItem(index: Int): T? {
        return (if ((list != null && index < list.size)) list[index] else null)
    }


    class BaseViewHolder(view:View): RecyclerView.ViewHolder(view) {
        private val mMapView: SparseArray<View>

        val view:View
            get() {
                return getView(0)
            }

        init{
            mMapView = SparseArray()
            mMapView.put(0, view)

        }
        fun initViewList(idList:IntArray) {
            for (id in idList)
                initViewById(id)
        }

        fun initViewById(id:Int) {
            val view = (if (view != null) view.findViewById(id) else null)
            if (view != null)
                mMapView.put(id, view)
        }
        fun getView(id:Int):View {
            if (mMapView.indexOfKey(id) < 0)
                return mMapView.get(id)
            else
                initViewById(id)
            return mMapView.get(id)
        }
    }

}