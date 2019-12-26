package com.dylanbui.routerapp.typicode.google_map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bluelinelabs.conductor.Controller
import com.dylanbui.android_library.DbAutoCompleteTextView
import com.dylanbui.android_library.placesautocomplete.*
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.utils.toast

class PlaceAutoCompleteViewController: Controller() {

    // var placesApi: PlaceAPI? = null

    lateinit var placesApi: PlaceAPI

    var street = ""
    var city = ""
    var state = ""
    var country = ""
    var zipCode = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var view = inflater.inflate(R.layout.controller_place_auto_complete, container, false)
        onViewBound(view)
        return view
    }

    fun onViewBound(view: View) {

        placesApi = PlaceAPI.Builder()
            .apiKey("AIzaSyDDOSDICyPGej1Iku5Z7uj0V_LHFQcmFDk")
            .build(activity?.baseContext!!)

        var autoCompleteEditText = view.findViewById<DbAutoCompleteTextView>(R.id.autoCompleteEditText)
        autoCompleteEditText.setAdapter(PlacesAutoCompleteAdapter(activity?.baseContext!!, placesApi))
        autoCompleteEditText.setLoadingIndicator(view.findViewById<ProgressBar>(R.id.pb_loading_indicator))
        autoCompleteEditText.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->

                val place = parent.getItemAtPosition(position) as Place
                activity?.toast(place.id)

//                autoCompleteEditText.setText(place.description)
//                getPlaceDetails(place.id)
            }


    }

    private fun getPlaceDetails(placeId: String) {
        placesApi.fetchPlaceDetails(placeId, object : OnPlacesDetailsListener {
            override fun onError(errorMessage: String) {
                activity?.toast(errorMessage)
            }

            override fun onPlaceDetailsFetched(placeDetails: PlaceDetails) {
                setupUI(placeDetails)
            }
        })
    }

    private fun setupUI(placeDetails: PlaceDetails) {
        val address = placeDetails.address
        parseAddress(address)

        var streetTextView: TextView = view!!.findViewById(R.id.streetTextView)
        var cityTextView: TextView = view!!.findViewById(R.id.cityTextView)
        var stateTextView: TextView = view!!.findViewById(R.id.stateTextView)
        var countryTextView: TextView = view!!.findViewById(R.id.countryTextView)

        var latitudeTextView: TextView = view!!.findViewById(R.id.latitudeTextView)
        var longitudeTextView: TextView = view!!.findViewById(R.id.longitudeTextView)

        activity?.runOnUiThread {

            streetTextView.text = street
            cityTextView.text = city
            stateTextView.text = state
            countryTextView.text = country
            // zipCodeTextView.text = zipCode
            latitudeTextView.text = placeDetails.lat.toString()
            longitudeTextView.text = placeDetails.lng.toString()
            // placeIdTextView.text = placeDetails.placeId
            // urlTextView.text = placeDetails.url
            // utcOffsetTextView.text = placeDetails.utcOffset.toString()
            // vicinityTextView.text = placeDetails.vicinity
            // compoundCodeTextView.text = placeDetails.compoundPlusCode
            // globalCodeTextView.text = placeDetails.globalPlusCode
        }
    }

    private fun parseAddress(address: ArrayList<Address>) {
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