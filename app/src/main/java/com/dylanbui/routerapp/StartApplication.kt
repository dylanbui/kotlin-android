package com.dylanbui.routerapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.dylanbui.routerapp.networking.AppNetwork
import com.dylanbui.routerapp.utils.Utils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class StartApplication : Application() {

    companion object {
        var compositeDisposable: CompositeDisposable? = null
        lateinit var context: Context
        var deviceToken: String? = null
        var deviceName: String? = null
        var versionApp: String? = null
        var versionName: String? = null
//        var user: User? = null
//        var profile: Profile? = null
        var imageFolder = ""
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        AppNetwork.setBaseUrl("http://45.117.162.50:8080/file/api/")

//        var okHttpClient = OkHttpClient().newBuilder()
//            .addInterceptor(ChuckInterceptor(this))
//            .build()
//        AndroidNetworking.initialize(applicationContext, okHttpClient)
////        if (BuildConfig.BUILD_TYPE.equals("debug"))
//        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY)
//
//        PreUtil.init(this)
//
//        Logger.addLogAdapter(AndroidLogAdapter())
//
//        user = PreUtil.getObject(PreUtil.KEY_USER, User::class.java)
//        profile = PreUtil.getObject(PreUtil.KEY_PROFILE, Profile::class.java)

        versionApp = Utils.getVersionApp(this)
        deviceName = Utils.getDeviceName()
        versionName = Utils.getVersionName()
//        deviceToken = Utils.getDeviceToken() // Pai co Firebase
//        Realm.init(this)
//        val config = RealmConfiguration.Builder().name("surveyapp.realm").build()
//        Realm.setDefaultConfiguration(config)
    }

}