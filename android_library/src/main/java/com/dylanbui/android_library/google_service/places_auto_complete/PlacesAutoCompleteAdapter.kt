package com.dylanbui.android_library.google_service.places_auto_complete

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.dylanbui.android_library.R
import com.dylanbui.android_library.google_service.DbGoogleServices
import com.dylanbui.android_library.google_service.GgPlace
import com.google.gson.JsonObject

// https://github.com/mukeshsolanki/Google-Places-AutoComplete-EditText

interface PlacesAutoComplete {
    fun placesAutoCompleteRowFormat(position: Int, place: GgPlace, convertView: View?): View
}

class PlacesAutoCompleteAdapter(mContext: Context, val placesApi: DbGoogleServices) :
    ArrayAdapter<GgPlace>(mContext, R.layout.autocomplete_list_item), Filterable {

    var resultList: ArrayList<GgPlace>? = ArrayList()
    var formatRow: PlacesAutoComplete? = null

    override fun getCount(): Int {
        return when {
            resultList.isNullOrEmpty() -> 0
            else -> resultList?.size!!
        }
    }

    override fun getItem(position: Int): GgPlace? {
        return when {
            resultList.isNullOrEmpty() -> null
            else -> resultList!![position]
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val place = resultList!![position]

        formatRow?.let {
            return it.placesAutoCompleteRowFormat(position, place, convertView)
        }

        var view = convertView
        val viewHolder: ViewHolder
        if (view == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.autocomplete_list_item, parent, false)
            viewHolder.description = view.findViewById(R.id.autocompleteText) as TextView
            viewHolder.footerImageView = view.findViewById(R.id.footerImageView) as ImageView
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        bindView(viewHolder, place, position)
        return view!!
    }

    private fun bindView(viewHolder: ViewHolder, place: GgPlace, position: Int) {
        if (!resultList.isNullOrEmpty()) {
            if (position != resultList!!.size - 1) {
                viewHolder.description?.text = place.descriptionPlace
                viewHolder.footerImageView?.visibility = View.GONE
                viewHolder.description?.visibility = View.VISIBLE
            } else {
                viewHolder.footerImageView?.visibility = View.VISIBLE
                viewHolder.description?.visibility = View.GONE
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                constraint?.let {
                    if (it.length > 3) {
                        placesApi.requestPlaces(it.toString(), asyncTask = false, complete = { arrPlace ->
                            resultList = arrPlace
                        })
                        // resultList = placesApi.autocomplete(it.toString())
                        // resultList?.add(Place("-1", "footer"))
                        resultList?.add(GgPlace(JsonObject()))
                        filterResults.values = resultList
                        filterResults.count = resultList!!.size
                    }
                }
                return filterResults
            }
        }
    }

    internal class ViewHolder {
        var description: TextView? = null
        var footerImageView: ImageView? = null
    }
}