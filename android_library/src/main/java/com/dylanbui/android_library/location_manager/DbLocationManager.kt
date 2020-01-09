package com.dylanbui.android_library.location_manager

import android.app.Activity
import android.content.Intent
import android.location.Location
import com.google.android.gms.location.LocationRequest

// -- Base on: https://github.com/grumpyshoe/android-module-locationmanager --

/**
 * <p>LocationTrackerConfig is a wrapper for all information fpr the tracker.</p>
 *
 * @since    1.0.0
 * @version  1.0.0
 * @author   grumpyshoe
 *
 */
data class DbLocationTrackerConfig(val interval: Long = 10000,
                                   val fastestInterval: Long = 5000L,
                                   val priority: Int = LocationRequest.PRIORITY_HIGH_ACCURACY)

/**
 * <p>LocationManager - interface for easy access to location handling</p>
 *
 * @since    1.0.0
 * @version  1.0.0
 * @author   grumpyshoe
 *
 */
interface DbLocationManager {
    /**
     * get last known location
     *
     */
    fun getLastKnownPosition(activity: Activity, onLastLocationFound: ((Location) -> Unit)?, onNoLocationFound: (() -> Unit)?)
    /**
     * get current location
     *
     */
    fun getCurrentLocation(activity: Activity, onLocation: (Location) -> Unit)
    /**
     * handle permission request result
     *
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean?
    /**
     * start location change tracker
     *
     */
    fun startLocationTracker(activity: Activity, config: DbLocationTrackerConfig, onLocationChange: (Location) -> Unit)
    /**
     * stop location change tracker
     *
     */
    fun stopLocationTracker()
    /**
     * handle onActivityResult for location settings resolver
     *
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean?
}



