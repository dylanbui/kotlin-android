package com.dylanbui.android_library.utils_adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface OnDbAdapterListener<in T> {

    fun onSelectedItemListener(model: T, index: Int, view: View? = null)

    fun onSelectedItemLongClickListener(model: T, index: Int, view: View? = null)

    fun onBottomReachedListener(model: T, index: Int)

}


//abstract class DbBaseRecyclerViewHolder<in T>(v: View): RecyclerView.ViewHolder(v) {
//    abstract fun onBind(data: T)
//}

// var adapterListener: OnDbAdapterListener<T>? = null,
// @LayoutRes val toBeInflated: Int,

abstract class DbBaseRecyclerAdapter<T>(var dataSet: ArrayList<T> = arrayListOf()) : RecyclerView.Adapter<DbBaseRecyclerAdapter.BaseViewHolder>() {

    protected abstract fun onCreateView(parent: ViewGroup, viewType: Int) : BaseViewHolder
    protected abstract fun onBindView(item: T, position: Int, viewHolder: BaseViewHolder)

    override fun getItemCount() = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        // val v = LayoutInflater.from(parent.context).inflate(toBeInflated, parent, false)
        return onCreateView(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        onBindView(dataSet[position], position, holder)
    }

    fun replaceData(data: ArrayList<T>) {
        dataSet = data// .toMutableList()
        notifyDataSetChanged()
    }

    fun updateData(data: ArrayList<T>) {
        dataSet.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        dataSet.clear()
        notifyDataSetChanged()
    }

    fun get(position: Int): T {
        return dataSet[position]
    }

    fun add(item: T) {
        dataSet.add(item)
        notifyItemInserted(dataSet.size - 1)
    }

    fun removeAt(index: Int) {
        dataSet.removeAt(index)
        notifyItemRemoved(index)
    }

    fun removeAll() {
        val size = dataSet.size
        dataSet.clear()
        notifyItemRangeRemoved(0, size)
    }


    class BaseViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }


}