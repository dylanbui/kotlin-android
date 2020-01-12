package com.dylanbui.routerapp

import android.app.Application
import android.content.Context
import com.dylanbui.android_library.utils.DbDeviceUtils
import com.dylanbui.routerapp.networking.AppNetwork
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable

class User {
    @SerializedName("userId")
    @Expose
    var userId: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("token")
    @Expose
    var token: String? = null

    override fun toString(): String {
        return "Post{" +
                ", userId = " + userId +
                ", name = " + name +
                ", email = '" + email + '\'' +
                ", phone = '" + phone + '\'' +
                '}'
    }

    fun isLogin(): Boolean {
        if (token == null) {
            return false
        }
        return true
    }

}

class StartApplication : Application() {

    companion object {
        var compositeDisposable: CompositeDisposable? = null
        lateinit var context: Context
        var deviceToken: String? = null
        var deviceName: String? = null
        var versionApp: String? = null
        var versionName: String? = null
        var currentUser: User = User()
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

        versionApp = DbDeviceUtils.getVersionApp(this)
        deviceName = DbDeviceUtils.getDeviceName()
        versionName = DbDeviceUtils.getVersionName()
//        deviceToken = Utils.getDeviceToken() // Pai co Firebase
//        Realm.init(this)
//        val config = RealmConfiguration.Builder().name("surveyapp.realm").build()
//        Realm.setDefaultConfiguration(config)
    }

}