package com.dylanbui.routerapp

import android.app.Application
import android.content.Context
import com.dylanbui.routerapp.networking.AppNetwork
import com.dylanbui.routerapp.utils.Utils
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable

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

        // Debug picasso
        val picasso = Picasso.Builder(applicationContext)
            .indicatorsEnabled(true)
            .loggingEnabled(true) //add other settings as needed
            .build()
        Picasso.setSingletonInstance(picasso) //apply to default singleton instance

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