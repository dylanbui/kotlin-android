package com.dylanbui.routerapp.typicode.bottom_sheet

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.dylanbui.android_library.utils.show
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.retrofit.BaseRecyclerViewAdapter
import com.dylanbui.routerapp.retrofit.BaseRecyclerViewHolder


// https://github.com/Commit451/ModalBottomSheetDialogFragment/blob/master/modalbottomsheetdialogfragment/src/main/java/com/commit451/modalbottomsheetdialogfragment/ModalBottomSheetDialogFragment.kt
//


interface IOptionItem {
    val id: Int
    var title: CharSequence
    var subTitle: CharSequence?
    var icon: Drawable?
}

class DbOption(override val id: Int, override var title: CharSequence) : IOptionItem {
    override var subTitle: CharSequence? = null
    override var icon: Drawable? = null
}

class DbOptionListAdapter(context: Context, listener: OptionRowListener? = null) :
    BaseRecyclerViewAdapter<IOptionItem>(
        dataSet = arrayListOf(),
        toBeInflated = R.layout.item_my_option,
        createHolder = { v -> ViewHolder(v) }) {

    private val context: Context = context
    private val mListener = listener

    //this method is binding the data on the list

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<IOptionItem>, position: Int) {
        // super.onBindViewHolder(holder, position)
        var myViewHolder = holder as? ViewHolder
        myViewHolder?.let {
            myViewHolder.bindItems(context, position, this.dataSet[position], mListener)
        }
    }



    //the class is hodling the list view
    class ViewHolder(itemView: View) : BaseRecyclerViewHolder<IOptionItem>(itemView) {
        private val title: TextView = itemView.findViewById(R.id.optionTitle)
        private val icon: ImageView = itemView.findViewById(R.id.optionIcon)

        private val layout: LinearLayout = itemView.findViewById(R.id.optionItemRow)

        fun bindItems(context: Context, position: Int, user: IOptionItem, listener: OptionRowListener?) {
            layout.setOnClickListener {
                listener?.onRowClick(position, user)
            }

            onBind(user)
        }

        override fun onBind(data: IOptionItem) {
            title.text = "${data.title}"

            if (data.icon != null) {
                icon.visibility = View.VISIBLE
                icon.setImageDrawable(data.icon)
            }

        }
    }

    interface OptionRowListener {
        fun onRowClick(position: Int, user: IOptionItem)
    }
}