package com.dylanbui.routerapp.typicode.google_map

import android.Manifest
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.dylanbui.android_library.location_manager.DbLocationManager
import com.dylanbui.android_library.location_manager.DbLocationManagerImpl
import com.dylanbui.android_library.utils.Utils
import com.dylanbui.android_library.utils.dLog
import com.dylanbui.android_library.utils.toast
import com.dylanbui.routerapp.BaseMvpController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dylanbui.routerapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


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
    private lateinit var btnFab: FloatingActionButton
    private lateinit var btnLocation: Button
    private lateinit var btnCameraPermission: Button
    private val locationManager : DbLocationManager = DbLocationManagerImpl()

    override fun setTitle(): String? = "GoogleMap - Controller"

    override fun createPresenter(): GoogleMapPresenter = GoogleMapPresenter()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_google_map, container, false)
    }

    override fun onViewBound(view: View) {

        ggMapView = view.findViewById(R.id.ggMapView) as MapView
        ggMapView.onCreate(null)
        ggMapView.getMapAsync(this)

        // Cach nao cung dung duoc
//        val mapFragment = (activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.ggMapFragment) as SupportMapFragment
//        mapFragment.getMapAsync(this)

        btnFab = view.findViewById(R.id.btnFab)
        btnFab.setOnClickListener { _ ->
            activity?.toast("setOnClickListener")
        }

        btnLocation = view.findViewById(R.id.btnLocation)
        btnLocation.setOnClickListener { _ ->
            locationManager.getCurrentLocation(activity = this.activity!!) {
                // handle location data
                moveToLocation("Vi tri hien tai", LatLng(it.latitude, it.longitude))
            }
        }

        btnCameraPermission = view.findViewById(R.id.btnCameraPermission)
        btnCameraPermission.setOnClickListener { _ ->



            mainActivity!!.managePermissions.checkPermissions(
                activity = this.activity!!,
                permissions = arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),
                onPermissionResult = { permissionResult ->
                    // handle permission result
                    dLog("getGranted: " + permissionResult.getGranted().toString())
                    dLog("getDenied: " + permissionResult.getDenied().toString())
                    if(permissionResult.areAllGranted()){
                        // Do the task now
                        activity?.toast("Permissions granted.")
                    }else{
                        activity?.toast("Permissions denied.")
                    }
                }, requestCode = 222)
        }

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
        val sydney = LatLng(10.764291,106.654309)
        val markerPropzy = MarkerOptions().position(sydney).title("Propzy")
        ggMap.addMarker(markerPropzy)
        ggMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

//        Utils.delayFunc(3000) {
//            // Updates the location and zoom of the MapView
//            moveToLocation("Vi tri moi", LatLng(43.1, -87.9))
//        }

        // Dung thang nay phai cap quyen truoc
        //ggMap.isMyLocationEnabled = true

    }

    fun moveToLocation(title: String, location: LatLng) {
        // Updates the location and zoom of the MapView
        val markerPos = MarkerOptions().position(location).title(title)
        ggMap.addMarker(markerPos)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 16f)
        ggMap.animateCamera(cameraUpdate)
    }

}

