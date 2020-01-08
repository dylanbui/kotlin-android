package com.dylanbui.routerapp.typicode.google_map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/*

onCreateView -> create the mapview, then call onCreate on it
onAttach -> call onResume
onDetach -> call onPause
onDestroyView -> call onDestroy
onSaveViewState -> call onSaveInstanceState

// https://stackoverflow.com/questions/16536414/how-to-use-mapview-in-android-using-google-map-v2

* */


class GoogleMapViewController : BaseMvpController<GoogleMapViewAction, GoogleMapPresenter>(),
    GoogleMapViewAction, OnMapReadyCallback {

    private lateinit var ggMap: GoogleMap
    private lateinit var ggMapView: MapView


    override fun setTitle(): String? = "GoogleMap"

    override fun createPresenter(): GoogleMapPresenter = GoogleMapPresenter()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_google_map, container, false)
    }

    override fun onViewBound(view: View) {

        ggMapView = view.findViewById(R.id.ggMapView) as MapView
        ggMapView.onCreate(null)
        ggMapView.onStart()
        ggMapView.getMapAsync(this)

    }

    override fun onPreAttach() {
        // -- At here presenter is existed --
        // -- Get action in row => presenter --
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        ggMapView.onResume()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        ggMapView.onPause()
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        ggMapView.onDestroy()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        ggMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        ggMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        ggMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

}

