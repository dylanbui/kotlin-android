package com.dylanbui.routerapp.typicode.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dylanbui.android_library.utils.onClick
import com.dylanbui.android_library.utils_adapter.DbBaseRecyclerAdapter
import com.dylanbui.android_library.utils_adapter.OnDbAdapterListener
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.typicode.TyPost
import kotlinx.android.synthetic.main.item_post.view.*


class PostListAdapter(var listener: OnDbAdapterListener<TyPost>? = null) : DbBaseRecyclerAdapter<TyPost>()
{
    var presenter: PostListPresenter? = null

    override fun onCreateView(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        val viewHolder = BaseViewHolder(view)

        // Gan cac su kien, khong set data field
        // Set gia tri data field trong onBindView
        // Truy cap tu bien viewHolder.itemView
        viewHolder.itemView.parentRow?.onClick {
            // Add su kien at here
            val position = viewHolder.adapterPosition // Lay dong hien tai
            val model = get(position)
            //TODO not implemented
            listener?.onSelectedItemListener(model, position, it)
        }

        return viewHolder
    }

    override fun onBindView(item: TyPost, position: Int, viewHolder: DbBaseRecyclerAdapter.BaseViewHolder) {
        viewHolder.itemView.title?.text = "Id: -- ${item.id.toString()} -- ${item.title}"
        viewHolder.itemView.body?.text = item.body
    }

}


//class PostListAdapter(context: Context) :
//    BaseRecyclerViewAdapter<TyPost>(
//        dataSet = arrayListOf<TyPost>(),
//        toBeInflated = R.layout.item_post,
//        createHolder = { v -> ViewHolder(v) }) {
//
//    private val context: Context = context
//    var presenter: PostListPresenter? = null
//
//    //this method is binding the data on the list
//
//    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<TyPost>, position: Int) {
//        // super.onBindViewHolder(holder, position)
//        var myViewHolder = holder as? ViewHolder
//        myViewHolder?.let {
//            myViewHolder.bindItems(context, position, this.dataSet[position], presenter)
//        }
//    }
//
//    //the class is holding the list view
//    class ViewHolder(itemView: View) : BaseRecyclerViewHolder<TyPost>(itemView) {
//        private val txtTitle: TextView = itemView.findViewById(R.id.title)
//        private val txtBody: TextView = itemView.findViewById(R.id.body)
//
//        private val layout: LinearLayout = itemView.findViewById(R.id.parentRow)
//
//        fun bindItems(context: Context, position: Int, post: TyPost, presenter: PostListPresenter? = null) {
//            layout.setOnClickListener { v: View? ->
//                presenter?.onPostRowClick(position, post)
//            }
//
//            onBind(post)
//        }
//
//        override fun onBind(data: TyPost) {
//            txtTitle.text = "Id: -- ${data.id.toString()} -- ${data.title}"
//            txtBody.text = data.body
//        }
//    }
//
//}