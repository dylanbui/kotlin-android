package com.dylanbui.routerapp.retrofit

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseRecyclerViewHolder<in T>(v: View): RecyclerView.ViewHolder(v){
    abstract fun onBind(data: T)
}

abstract class BaseRecyclerViewAdapter<T>(var dataSet: MutableList<T>, @LayoutRes val toBeInflated: Int, val createHolder: (v: View) -> BaseRecyclerViewHolder<T>) : RecyclerView.Adapter<BaseRecyclerViewHolder<T>>(){
    override fun getItemCount() = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<T> {
        val v = LayoutInflater.from(parent.context).inflate(toBeInflated, parent, false)
        return createHolder(v)

    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<T>, position: Int) {
        holder.onBind(dataSet[position])
    }

    fun replaceData(data: MutableList<T>) {
        dataSet = data.toMutableList()
        notifyDataSetChanged()
    }

    fun updateData(data: MutableList<T>) {
        dataSet.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        dataSet.clear()
        notifyDataSetChanged()
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


}