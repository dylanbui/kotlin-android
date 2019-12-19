package com.dylanbui.routerapp.typicode.user

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.retrofit.BaseRecyclerViewAdapter
import com.dylanbui.routerapp.retrofit.BaseRecyclerViewHolder
import com.dylanbui.routerapp.typicode.TyUser

class UserListAdapter(context: Context, listener: UserRowListener? = null) :
    BaseRecyclerViewAdapter<TyUser>(
        dataSet = arrayListOf(),
        toBeInflated = R.layout.item_user,
        createHolder = { v ->
            ViewHolder(
                v
            )
        }) {

    private val context: Context = context
    private val mListener = listener

    //this method is binding the data on the list

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<TyUser>, position: Int) {
        // super.onBindViewHolder(holder, position)
        var myViewHolder = holder as? ViewHolder
        myViewHolder?.let {
            myViewHolder.bindItems(context, position, this.dataSet[position], mListener)
        }
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : BaseRecyclerViewHolder<TyUser>(itemView) {
        private val txtId: TextView = itemView.findViewById(R.id.userId)
        private val txtName: TextView = itemView.findViewById(R.id.name)
        private val txtEmail: TextView = itemView.findViewById(R.id.email)

        private val layout: LinearLayout = itemView.findViewById(R.id.parentRow)

        fun bindItems(context: Context, position: Int, user: TyUser, listener: UserRowListener?) {
            layout.setOnClickListener { v: View? ->
                listener?.onRowClick(position, user)
            }

            onBind(user)
        }

        override fun onBind(data: TyUser) {
            txtId.text = "Id: -- ${data.id.toString()}"
            txtName.text = data.name
            txtEmail.text = data.email
        }
    }

    interface UserRowListener {
        fun onRowClick(position: Int, user: TyUser)
    }
}