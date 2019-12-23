package com.dylanbui.routerapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

@SuppressLint("all")
object DeviceUtils {

    @SuppressLint("MissingPermission")
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        //should check null because in airplane mode it will be null
        return netInfo != null && netInfo.isConnected
    }

    fun getVersionName(): String = Build.VERSION.RELEASE

    fun getVersionApp(context: Context): String {
        var result = ""
        try {
            val manager = context.packageManager
            var info: PackageInfo? = null
            info = manager.getPackageInfo(
                context.packageName, 0
            )
            result = info!!.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return result
    }

    fun getWidthScreen(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun getHeightScreen(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            Utils.capitalize(model)
        } else {
            Utils.capitalize(manufacturer) + " " + model
        }
    }

    fun getDeviceToken(): String? {
        var token: String? = ""
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(Constants.TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                token = task.result?.token
            })
        return token
    }
}