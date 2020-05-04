package com.dylanbui.routerapp.networking

import android.util.Log
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

inline fun <reified T> className() = Class.forName(T::class.qualifiedName) as Class


//inline fun <reified T: IResponse> makeThisInstance(): T {
//    // return T::class.java.newInstance()
//
//    val e = className<T>().newInstance() as T
//    // e.parseJsonString("")
//    return e
////    if (e is IResponse) {
////        e.parseJsonString("")
////    }
////    return e.newInstance() as T
//
//}

interface BaseService<T> {

    // @Headers("Content-Type: application/json")
    @GET
    suspend fun makeGetRequest(@Url url: String?): Response<T>

    // At more PATH, PUT, UPLOAD
}

interface TestApi: BaseService<CloudResponse> {

}

// https://stackoverflow.com/questions/59522202/my-generic-function-for-retrofit-interface-creation-get-compiler-error-inferred

class GenericNetwork {

    enum class Method(val method: String) {
        GET("GET"),
        POST("POST"),
    }



    private var BASE_URL = "http://45.117.162.60:8080/diy/api/"

    private fun <T> createService(interfaceClazz: Class<T>) : T {
        Log.d("WebAccess", "Creating retrofit client")

        // -- Log for Retrofit --
        var httpLoggingInterceptor = HttpLoggingInterceptor()
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder().baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        // Create Retrofit client
        return retrofit.create(interfaceClazz)
        //return retrofit.create(TestApi::class.java)
    }

    fun create() = GlobalScope.launch {
        val sService = createService(TestApi::class.java)
        val response = sService.makeGetRequest("")
        response.body()
    }





}