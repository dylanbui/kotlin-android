package com.dylanbui.routerapp.controller.user

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dylanbui.routerapp.R
import kotlinx.android.parcel.Parcelize



@Parcelize
data class User(val id: Int, val name: String, val email: String) : Parcelable


class UsersAdapter(context: Context, listener: UserRowListener? = null):
    RecyclerView.Adapter<UsersAdapter.ViewHolder>()
{
    private var listUser: MutableList<User> = arrayListOf()
    private val context: Context = context
    private val mListener = listener

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_user, parent, false))
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bindItems(context,position, this.listUser[position], mListener)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return listUser.size
    }

    fun updateData(list: MutableList<User>) {
        this.listUser.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData() {
        listUser.clear()
        notifyDataSetChanged()
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val txtName: TextView = itemView.findViewById(R.id.name)
        private val txtEmail: TextView = itemView.findViewById(R.id.email)

        private val layout: LinearLayout = itemView.findViewById(R.id.parentRow)


        fun bindItems(context: Context, position: Int, user: User, listener: UserRowListener?) {
            txtName.text = user.name
            txtEmail.text = user.email

            layout.setOnClickListener { v: View? ->
                listener?.onRowClick(position, user)
            }
        }
    }

    interface UserRowListener {
        fun onRowClick(position: Int, user: User)
    }
}