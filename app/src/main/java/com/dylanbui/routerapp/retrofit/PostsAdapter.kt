package com.dylanbui.routerapp.retrofit

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.dylanbui.routerapp.R

class PostsAdapter(context: Context, listener: PostRowListener? = null) :
    BaseRecyclerViewAdapter<Post>(
        dataSet = arrayListOf(),
        toBeInflated = R.layout.item_post,
        createHolder = { v -> PostsAdapter.ViewHolder(v) }) {

    private val context: Context = context
    private val mListener = listener

    //this method is binding the data on the list

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<Post>, position: Int) {
        // super.onBindViewHolder(holder, position)
        var myViewHolder = holder as? PostsAdapter.ViewHolder
        myViewHolder?.let {
            myViewHolder.bindItems(context, position, this.dataSet[position], mListener)
        }
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : BaseRecyclerViewHolder<Post>(itemView) {
        private val txtName: TextView = itemView.findViewById(R.id.title)
        private val txtEmail: TextView = itemView.findViewById(R.id.body)

        private val layout: LinearLayout = itemView.findViewById(R.id.parentRow)


        fun bindItems(context: Context, position: Int, post: Post, listener: PostRowListener?) {
            layout.setOnClickListener { v: View? ->
                listener?.onRowClick(position, post)
            }

            onBind(post)
        }

        override fun onBind(data: Post) {
            txtName.text = data.title
            txtEmail.text = data.body
        }
    }

    interface PostRowListener {
        fun onRowClick(position: Int, user: Post)
    }
}