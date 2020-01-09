package com.dylanbui.android_library.permission_manager

import android.app.Activity
import android.content.pm.PackageManager

// -- Base on: https://github.com/grumpyshoe/android-module-permissionmanager/ --

data class DbPermissionRequestExplanation(val title: String = "Hint", val message: String)

data class DbPermissionResult(val result: Map<String, Int>, val requestCode: Int) {
    /**
     * check if all permissions are granted
     *
     */
    fun areAllGranted(): Boolean {
        var allGranted = true
        result.values.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) {
                allGranted = false
            }
        }
        return allGranted
    }
    /**
     * check if at least one permission is granted
     *
     */
    fun isOneGranted(): Boolean {
        var oneGranted = false
        result.values.forEach {
            if (it == PackageManager.PERMISSION_GRANTED) {
                oneGranted = true
            }
        }
        return oneGranted
    }
    /**
     * get all granted permissions
     *
     */
    fun getGranted(): List<String> {
        return result.filter { it.value == PackageManager.PERMISSION_GRANTED }.map { it.key }
    }
    /**
     * get all denied permissions
     *
     */
    fun getDenied(): List<String> {
        return result.filter { it.value == PackageManager.PERMISSION_DENIED }.map { it.key }
    }
}

/**
 * <p>PermissionManager - interface for easy access to permission handling</p>
 *
 * @since    1.0.0
 * @version  1.0.0
 * @author   grumpyshoe
 *
 */
interface DbPermissionManager {


    companion object {
        const val DEFAULT_PERMISSION_REQUEST_CODE = 8102
    }

    /**
     * check permission
     *
     */
    fun checkPermissions(activity: Activity, permissions: Array<out String>,
                         onPermissionResult: ((DbPermissionResult) -> Unit)?,
                         permissionRequestPreExecuteExplanation: DbPermissionRequestExplanation? = null,
                         permissionRequestRetryExplanation: DbPermissionRequestExplanation? = null,
                         requestCode: Int? = null): Boolean


    /**
     * handle permission request result
     *
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean?


}