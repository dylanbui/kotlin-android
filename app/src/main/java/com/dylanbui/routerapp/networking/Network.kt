package com.dylanbui.routerapp.networking

import com.dylanbui.routerapp.retrofit.Post
import com.google.gson.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import java.lang.reflect.Type
import java.util.*
import java.util.Arrays.asList
import kotlin.collections.HashMap

class District {

//    "districtId": 1,
//    "countryId": 1,
//    "regionId": 3,
//    "cityId": 1,
//    "districtName": "Quận 1",
//    "districtShortName": "Quận 1",
//    "districtNameEn": "District 1",

    @SerializedName("districtId")
    @Expose
    var districtId: Int? = null

    @SerializedName("cityId")
    @Expose
    var cityId: Int? = null

    @SerializedName("districtName")
    @Expose
    var districtName: String? = null

    @SerializedName("districtNameEn")
    @Expose
    var districtNameEn: String? = null
}

interface MyNetworkingService {

//    @GET("/posts")
//    fun getCallPosts(): Call<List<Post>>
//
//
//    @GET("users/{user}/repos")
//    fun listRepos(@Path("user") user: String?): Call<List<User?>?>?
//
//    @POST("/make/posts")
//    fun makePosts(@Body body: JsonObject): Call<List<Post>>
//

    @POST("/make/posts")
    fun makePostsHasMap(@Body body: HashMap<String, Any?>): Call<List<Post>>

    @GET
    fun makeGetRequest(@Url url: String?): Call<CloudResponse>

    @POST
    fun makePostRequest(@Url url: String?,
                        @Body requestBody: RequestBody?): Call<CloudResponse>
}

interface IServiceCallBack<T> {
    fun onSuccess(result: T?, responseBody: CloudResponse)
    // fun onSuccess(responseCode:Int, responseMessage:String, responseBody: CloudResponse<T>?)

    fun onFailed(errorData: AppNetworkServiceError)
}

interface IServiceCallBackList<T> {
    fun onSuccess(result: List<T>?, responseBody: CloudResponse)
    fun onFailed(errorData: AppNetworkServiceError)
}

object JsonListHelper {
    @Throws(Exception::class)
//    fun <T> getList(json: JsonElement?): List<T> {
//        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
//        val typeOfList: Type = object : TypeToken<List<T>?>() {}.type
//        return gson.fromJson(json, typeOfList)
//}
    fun <T> getList(jsonArr: JsonArray): List<T> {
        val gSon = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        val people = mutableListOf<T>()
        // val typeOfObj: Type = object : TypeToken<T>() {}.type
        // val typeOfObj: T = genericType()
        // val turnsType: T = genericType()
        jsonArr.forEach { jsonObj ->
            // people.add(gSon.fromJson(jsonObj, typeOfObj))
//            var t: District = fromJson(jsonObj)
//            people.add(gSon.fromJson(jsonObj, typeOfObj))

        }
        return  people.toList()
    }


    // final inline fun <reified T> className() = Class.forName(T::class.qualifiedName) as Class
//    inline fun <reified T> genericType() = object: TypeToken<T>() {}.type
//    inline fun <reified T> genericObj(json: JsonElement) : T = this.fromJson<T>(json, T::class.java)
}



class MyNetworking private constructor() {


    fun start(): MyNetworkingService {
        return retrofit!!.create(MyNetworkingService::class.java)
    }

    companion object {
        private var instance: MyNetworking? = null
        private var retrofit: Retrofit? = null
        private lateinit var builder: Retrofit.Builder
        // private const val BASE_URL = "https://jsonplaceholder.typicode.com"
        private const val BASE_URL = "http://45.117.162.60:8080/diy/api/"


        @Synchronized
        fun getInstance(): MyNetworking {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            if (instance == null) {
                instance = MyNetworking()
            }
            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).client(client).build()
            }

            return instance as MyNetworking
        }

    }

    fun request(path: String, onResponse: onResponseSuccess, onFailed: onResponseFailed) {
        var call: Call<CloudResponse> = this.start().makeGetRequest(path)

        doCall(call, onResponse, onFailed)
    }

    private fun doCall(call: Call<CloudResponse>, onResponse: onResponseSuccess, onFailed: onResponseFailed) {
        call.enqueue(
            object: Callback<CloudResponse> {
                override fun onResponse(call: Call<CloudResponse>, response: Response<CloudResponse>) {
                    if (response.code() == 200) {
                        response.body()?.let {
                            // Xu ly cac loi cua CloudResponse tra ve tu server, goi ham neu can
                            // onFailed(MyNetworkingServiceError("123", "khong loi"))
                            onResponse(it)
                        }
                    } else {
                        // Co loi, tra ve loi o day
                    }
                }
                override fun onFailure(call: Call<CloudResponse>, t: Throwable) {
                    // Day la loi, xu ly loi o day
                    onFailed(AppNetworkServiceError("123", "khong loi"))
                }
            })
    }


//   fun <T> requestListaaaa(path: String, listener: ServiceCallBack<List<T>>) {
//        var call: Call<CloudResponse> = this.start().makeGetRequest(path)
//        call.enqueue(
//            object: Callback<CloudResponse> {
//                override fun onResponse(call: Call<CloudResponse>, response: Response<CloudResponse>) {
//                    if (response.code() == 200) {
//                        response.body()?.let {
//                            println("chay ngon lanh")
//
//                            if (it.data is JsonNull) {
//                                 println("JsonNull")
//                            }
//
//                            if (it.data is JsonObject) {
//                                println("JsonObject")
//                            }
//
//                            if (it.data is JsonArray) {
//                                // var data = it.data as? JsonArray
//                                it.data?.let {
//
////                                    val myList: List<T> = JsonListHelper.getList(it)
////                                    listener.onSuccess(myList, response.body()!!)
////                                    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
////                                    val typeOfList: Type = object : TypeToken<List<T>?>() {}.type
////                                    val myList = gson.fromJson(it, typeOfList)
////
////                                    //val myList = gson.fromJson(it, Array<T::class>::class.java).toList()
//
//                                    return
//                                }
//
//                            }
//
//                            listener.onSuccess(null, response.body()!!)
//                        }
//                    }
//
//                }
//                override fun onFailure(call: Call<CloudResponse>, t: Throwable) {
//
//                }
//            })
//
//    }

//    fun request(path: String, listener: ServiceCallBack<List<District>>)
//    {
//        //Call<ResponseBody> request = serviceCaller.getSomeInfo(userid);
//        var call: Call<CloudResponse> = this.start().makeGetRequest(path)
//        call.enqueue(
//            object: Callback<CloudResponse> {
//                override fun onResponse(call: Call<CloudResponse>, response: Response<CloudResponse>) {
//                if (response.code() == 200) {
//                    response.body()?.let {
//                        println("chay ngon lanh")
//
//                        if (it.data is JsonNull) {
//                            println("JsonNull")
//                        }
//
//                        if (it.data is JsonObject) {
//                            println("JsonObject")
//                        }
//
//                        if (it.data is JsonArray) {
//                            // var data = it.data as? JsonArray
//                            it.data?.let {
//                                val gson = GsonBuilder().create()
//                                val myList = gson.fromJson(it, Array<District>::class.java).toList()
//                                myList.forEach {
//                                    println("Price of book, ${it.districtId} is ${it.districtName}")
//                                }
//                                listener.onSuccess(myList, response.body()!!)
//                                return
//                            }
//
//                        }
//
//                        listener.onSuccess(null, response.body()!!)
//                    }
//                }
//
//                }
//                override fun onFailure(call: Call<CloudResponse>, t: Throwable) {
//
//                }
//            })
//    }


}