package com.dylanbui.routerapp.typicode.post

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.retrofit.BaseRecyclerViewAdapter
import com.dylanbui.routerapp.retrofit.BaseRecyclerViewHolder
import com.dylanbui.routerapp.typicode.TyPost

class PostListAdapter(context: Context) :
    BaseRecyclerViewAdapter<TyPost>(
        dataSet = arrayListOf<TyPost>(),
        toBeInflated = R.layout.item_post,
        createHolder = { v -> ViewHolder(v) }) {

    private val context: Context = context
    var presenter: PostListPresenter? = null

    //this method is binding the data on the list

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<TyPost>, position: Int) {
        // super.onBindViewHolder(holder, position)
        var myViewHolder = holder as? ViewHolder
        myViewHolder?.let {
            myViewHolder.bindItems(context, position, this.dataSet[position], presenter)
        }
    }

    //the class is holding the list view
    class ViewHolder(itemView: View) : BaseRecyclerViewHolder<TyPost>(itemView) {
        private val txtTitle: TextView = itemView.findViewById(R.id.title)
        private val txtBody: TextView = itemView.findViewById(R.id.body)

        private val layout: LinearLayout = itemView.findViewById(R.id.parentRow)

        fun bindItems(context: Context, position: Int, post: TyPost, presenter: PostListPresenter? = null) {
            layout.setOnClickListener { v: View? ->
                presenter?.onPostRowClick(position, post)
            }

            onBind(post)
        }

        override fun onBind(data: TyPost) {
            txtTitle.text = "Id: -- ${data.id.toString()} -- ${data.title}"
            txtBody.text = data.body
        }
    }

}