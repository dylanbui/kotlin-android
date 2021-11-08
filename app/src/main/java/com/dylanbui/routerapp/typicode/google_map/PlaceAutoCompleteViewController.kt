package com.dylanbui.routerapp.typicode.google_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.TextView
import com.bluelinelabs.conductor.Controller
import com.dylanbui.android_library.google_service.DbGoogleServices
import com.dylanbui.android_library.google_service.GgAddress
import com.dylanbui.android_library.google_service.GgPlace
import com.dylanbui.android_library.google_service.GgPlaceDetail
import com.dylanbui.android_library.google_service.places_auto_complete.DbAutoCompleteTextView
import com.dylanbui.android_library.google_service.places_auto_complete.PlacesAutoCompleteAdapter
import com.dylanbui.android_library.utils.toast
import com.dylanbui.routerapp.R


class PlaceAutoCompleteViewController : Controller() {

    lateinit var placesApi: DbGoogleServices

    var street = ""
    var city = ""
    var state = ""
    var country = ""
    var zipCode = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var view = inflater.inflate(R.layout.controller_place_auto_complete, container, false)
        onViewBound(view)
        return view
    }

    fun onViewBound(view: View) {

        placesApi = DbGoogleServices.Builder()
            .apiKey("AIzaSyC512FiDx1hUcWlAGOpU6Kns2WruLPSIWk")
            .build(activity?.baseContext!!)

        var autoCompleteEditText = view.findViewById<DbAutoCompleteTextView>(R.id.autoCompleteEditText)
        autoCompleteEditText.setAdapter(PlacesAutoCompleteAdapter(activity?.baseContext!!, placesApi))
        autoCompleteEditText.setLoadingIndicator(view.findViewById<ProgressBar>(R.id.pb_loading_indicator))
        autoCompleteEditText.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val place = parent.getItemAtPosition(position) as GgPlace
                activity?.toast(place.placeId)

                autoCompleteEditText.setText(place.descriptionPlace)
                getPlaceDetails(place.placeId)
            }


        // var rxPermissions = RxPermissions(activity)
        //val rxPermissions = RxPermissions(this)


    }

    private fun getPlaceDetails(placeId: String) {
        placesApi.requestPlaceDetail(placeId, completion = { placeDetail ->

            placeDetail?.let {
                setupUI(it)
            }
        })
    }

    private fun setupUI(placeDetail: GgPlaceDetail) {

        var streetTextView: TextView = view!!.findViewById(R.id.streetTextView)
        var cityTextView: TextView = view!!.findViewById(R.id.cityTextView)
        var stateTextView: TextView = view!!.findViewById(R.id.stateTextView)
        var countryTextView: TextView = view!!.findViewById(R.id.countryTextView)

        var latitudeTextView: TextView = view!!.findViewById(R.id.latitudeTextView)
        var longitudeTextView: TextView = view!!.findViewById(R.id.longitudeTextView)

        activity?.runOnUiThread {
            streetTextView.text = placeDetail.formattedAddress
            cityTextView.text = placeDetail.name
            stateTextView.text = state
            countryTextView.text = country
            latitudeTextView.text = placeDetail.location.latitude.toString()
            longitudeTextView.text = placeDetail.location.longitude.toString()
        }
    }

    private fun parseAddress(address: ArrayList<GgAddress>) {
        (0 until address.size).forEach { i ->
            when {
                address[i].type.contains("street_number") -> street += address[i].shortName + " "
                address[i].type.contains("route") -> street += address[i].shortName
                address[i].type.contains("locality") -> city += address[i].shortName
                address[i].type.contains("administrative_area_level_1") -> state += address[i].shortName
                address[i].type.contains("country") -> country += address[i].shortName
                address[i].type.contains("postal_code") -> zipCode += address[i].shortName
            }
        }
    }


}