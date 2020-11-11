package com.dylanbui.android_library.ui_control.hint_spinner

import android.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.dylanbui.android_library.IDbItem

/**
 * Allows adding a hint at the end of the list. It will show the hint when adding it and selecting
 * the last object. Otherwise, it will show the dropdown view implemented by the concrete class.
 *
userHintSpinner = DbSpinnerAdapter<ChannelType>(chooseChannelType, object : HintAdapter<ChannelType>(activity,R.layout.item_channel_type_view,R.string.str_choose_channel_title,mChannelTypes) {

    override fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        var type = getItem(position)
        val view = inflateLayout(parent, false)
        view.title.text = type!!.name
        return view
    }
}, HintSpinner.Callback<ChannelType> { position, itemAtPosition ->
    channelTypeSelected = itemAtPosition
    })
    userHintSpinner.init()
 */

abstract class DbSpinnerAdapter<T: IDbItem>(context: Context, private val layoutResource: Int, private val hintResource: String?, var data: List<T>?) : ArrayAdapter<T>(context, layoutResource, data) {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    init {
        // Create a copy, as we need to be able to add the hint without modifying the array passed in
        // or crashing when the user sets an unmodifiable.
        // layoutInflater = LayoutInflater.from(context)
    }

    constructor(context: Context, hintResource: Int, data: List<T>?)
            : this(context, DEFAULT_LAYOUT_RESOURCE, context.getString(hintResource), data) {
    }

    constructor(context: Context, hint: String?, data: List<T>?)
            : this(context, DEFAULT_LAYOUT_RESOURCE, hint, data) {
    }

    constructor(context: Context, layoutResource: Int, hintResource: Int, data: List<T>?)
            : this(context, layoutResource, context.getString(hintResource), data) {
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    /**
     * Hook method to set a custom view.
     *
     * Provides a default implementation using the simple spinner dropdown item.
     *
     * @param position Position selected
     * @param convertView View
     * @param parent Parent view group
     */
    open fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflateDefaultLayout(parent)
        val item: IDbItem? = getItem(position)
        val textView = view.findViewById<View>(R.id.text1) as TextView
        textView.text = item?.getDisplay()
        textView.hint = ""
        return view
    }

    private fun inflateDefaultLayout(parent: ViewGroup): View {
        return inflateLayout(DEFAULT_LAYOUT_RESOURCE, parent, false)
    }

    private fun inflateLayout(resource: Int, root: ViewGroup, attachToRoot: Boolean): View {
        return layoutInflater.inflate(resource, root, attachToRoot)
    }

    fun inflateLayout(root: ViewGroup?, attachToRoot: Boolean): View {
        return layoutInflater.inflate(layoutResource, root, attachToRoot)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d(TAG, "position: $position, getCount: $count")
        val view: View = if (position == hintPosition) {
            getDefaultView(parent)
        } else {
            getCustomView(position, convertView, parent)
        }
        return view
    }

    private fun getDefaultView(parent: ViewGroup): View {
        val view = inflateDefaultLayout(parent)
        val textView = view.findViewById<View>(R.id.text1) as TextView
        textView.text = ""
        textView.hint = hintResource
        return view
    }

    /**
     * Gets the position of the hint.
     *
     * @return Position of the hint
     */
    val hintPosition: Int
        get() {
            val count = count
            return if (count > 0) count + 1 else count
        }

    companion object {
        private val TAG = DbHintAdapter::class.java.simpleName
        private const val DEFAULT_LAYOUT_RESOURCE = R.layout.simple_spinner_dropdown_item
    }

}