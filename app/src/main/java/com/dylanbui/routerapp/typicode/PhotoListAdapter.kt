package com.dylanbui.routerapp.typicode

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.retrofit.BaseRecyclerViewAdapter
import com.dylanbui.routerapp.retrofit.BaseRecyclerViewHolder
import com.squareup.picasso.Picasso

class PhotoListAdapter(context: Context, listener: PhotoRowListener? = null) :
    BaseRecyclerViewAdapter<TyPhoto>(
        dataSet = arrayListOf(),
        toBeInflated = R.layout.item_photo,
        createHolder = { v -> PhotoListAdapter.ViewHolder(v) }) {

    private val context: Context = context
    private val mListener = listener

    //this method is binding the data on the list

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<TyPhoto>, position: Int) {
        // super.onBindViewHolder(holder, position)
        var myViewHolder = holder as? PhotoListAdapter.ViewHolder
        myViewHolder?.let {
            myViewHolder.bindItems(context, position, this.dataSet[position], mListener)
        }
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : BaseRecyclerViewHolder<TyPhoto>(itemView) {
        private val txtTitle: TextView = itemView.findViewById(R.id.title)
        private val txtUrl: TextView = itemView.findViewById(R.id.url)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        private val layout: LinearLayout = itemView.findViewById(R.id.parentRow)

        fun bindItems(context: Context, position: Int, photo: TyPhoto, listener: PhotoRowListener?) {
            layout.setOnClickListener { v: View? ->
                // v?.setBackgroundColor(Color.BLUE)
                listener?.onRowClick(position, photo)
            }

            onBind(photo)
        }

        override fun onBind(data: TyPhoto) {
            txtTitle.text = "${data.id} -- ${data.title}"
            txtUrl.text = data.url
            Picasso.get().load(data.thumbnailUrl).into(imageView)
        }
    }

    interface PhotoRowListener {
        fun onRowClick(position: Int, photo: TyPhoto)
    }
}