package com.dylanbui.android_library

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions


object DbPermissions {

    fun requestAccessPhotoLibrary(activity: Activity, result: ((Boolean) -> Unit)) {
        RxPermissions(activity as AppCompatActivity)
            .request(
                Manifest.permission.CAMERA
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .subscribe({ grand ->
                result(grand)
            })
    }

    fun requestAccessLocation(activity: Activity, result: ((Boolean) -> Unit)) {
        RxPermissions(activity as AppCompatActivity)
            .request(
                Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
            )
            .subscribe({ grand ->
                result(grand)
            })
    }

}