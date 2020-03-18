package com.dylanbui.routerapp.networking

import android.util.Log
import com.dylanbui.android_library.utils.dLog
import com.dylanbui.routerapp.bgDispatcher
import com.dylanbui.routerapp.uiDispatcher
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


class ApiServiceError(var errorCode: Int, var errorMessage: String)
typealias PairResponse = Pair<CloudResponse?, ApiServiceError?>

data class TripleResult<T>(val list: T?, val totalItems: Int, val totalPages: Int)

interface ApiService {

    // @Headers("Content-Type: application/json")
    @GET
    suspend fun makeGetRequest(@Url url: String?): Response<CloudResponse>

    @POST
    suspend fun makePostRequest(@Url url: String?,
                        @Body requestBody: RequestBody?): Response<CloudResponse>

    @Multipart // don use with json
    @POST()
    suspend fun makeUploadFileRequest(@Url url: String?,
                              @PartMap() partMap: HashMap<String, RequestBody>?,
                              @Part file: MultipartBody.Part): Response<CloudResponse>

    // At more PATH, PUT, UPLOAD
}

object CoroutinesNetwork {

    enum class Method(val method: String) {
        GET("GET"),
        POST("POST"),
    }

    private var BASE_URL = "http://45.117.162.60:8080/diy/api/"

    private val makeApiService : ApiService by lazy {
        Log.d("WebAccess", "Creating retrofit client")

        // -- Log for Retrofit --
        var httpLoggingInterceptor = HttpLoggingInterceptor()
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

//        val interceptor = HttpLoggingInterceptor()
//        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            // .addInterceptor(interceptor)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

//        val retrofit = Retrofit.Builder()
//            // The 10.0.2.2 address routes request from the Android emulator
//            // to the localhost / 127.0.0.1 of the host PC
//            .baseUrl(BASE_URL)
//            // Moshi maps JSON to classes
//            .addConverterFactory(MoshiConverterFactory.create())
//            // The call adapter handles threads
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
//            .build()

        // Create Retrofit client
        return@lazy retrofit.create(ApiService::class.java)
    }

    // Set at Application()
    fun setBaseUrl(str: String) {
        BASE_URL = str
    }

    // Cac ham utility de xu ly du lieu tra ve

    inline fun <reified T> doRequest(path: String, method: Method, params: DictionaryType? = null,
                                     threadCallback: CoroutineDispatcher = uiDispatcher,
                                     crossinline onResponse: (responseBody: T?, errorData: ApiServiceError?) -> Unit
    ) = GlobalScope.launch(bgDispatcher) {

        var result: T? = null
        var returnError: ApiServiceError? = null
        // -- Call Synchronously Task --
        val (responseBody, error) = doExecute(path, method, params)

        if (responseBody != null) {
            try {
                result = Gson().fromJson(responseBody.data, object : TypeToken<T>() {}.type)
            } catch (e: JsonParseException) {
                returnError = ApiServiceError(404,
                    "JsonParseException: " + e.message)
            }
        } else {
            dLog("${error?.errorMessage}")
            // return Pair(null, error)
            returnError = error
        }

        // Update UI Thread
        withContext(threadCallback) {
            onResponse(result, returnError)
        }
    }


    inline fun <reified T> doRequestList(path: String, method: Method, params: DictionaryType? = null,
                                     threadCallback: CoroutineDispatcher = uiDispatcher,
                                         crossinline onResponse: (responseBody: TripleResult<T>?, errorData: ApiServiceError?) -> Unit
    ) = GlobalScope.launch(bgDispatcher) {

        var result: TripleResult<T>? = null
        var returnError: ApiServiceError? = null
        // -- Call Synchronously Task --
        val (responseBody, error) = doExecute(path, method, params)

        if (responseBody != null) {
            if (responseBody.data is JsonObject) {
                // Kiem tra dung loai du lieu
                val elementList = responseBody.data?.asJsonObject?.get("list")
                val totalItems = responseBody.data?.asJsonObject?.get("totalItems")
                val totalPages = responseBody.data?.asJsonObject?.get("totalPages")
                if (elementList != null && totalItems != null && totalPages != null) {
                    // val resultTT: T? = Gson().fromJson(elementList, object : TypeToken<T>() {}.type)
                    // val resultT: T? = DbJson.instance.fromJson(elementList, object : TypeToken<T>() {}.type)
                    try {
                        val resultT: T? = Gson().fromJson(elementList, object : TypeToken<T>() {}.type)
                        result = TripleResult(resultT, totalItems?.asInt ?: 0, totalPages?.asInt ?: 0)
                    } catch (e: JsonParseException) {
                        returnError = ApiServiceError(404,
                            "JsonParseException: " + e.message)
                    }
                } else {
                    returnError = ApiServiceError(404, "Json Parse Error")
                }
            } else {
                returnError = ApiServiceError(404, "Json Parse Error")
            }
        } else {
            dLog("${error?.errorMessage}")
            // return Pair(null, error)
            returnError = error
        }

        // Update UI Thread
        withContext(threadCallback) {
            onResponse(result, returnError)
        }
    }


    // Toan bo qua trinh goi deu la Synchronously

    // Ket qua tra ve luc nay, response da co gia tri, khong can kiem tra loi trong response
    suspend fun doExecute(path: String, method: Method, params: DictionaryType? = null): PairResponse {
        // Default set Method GET
        val response: Response<CloudResponse> = makeCall(path, method, params)

        // Xu ly ket qua tra ve
        //var result = PairResponse(null, AppServiceError("404", "Loi qua nng"))
        var result: CloudResponse? = null
        var error = ApiServiceError(404, "Loi qua nng")

        try {
            // if (response?.code() in 400..511)
            if (response.isSuccessful && response.body() != null) {
                // Kiem tra loi trong body response
                val responseBody = response.body()!!
                // Ket qua tra ve dung server, dung ket qua
                if (responseBody.result == true) {
                    result = responseBody
                } else {
                    error = ApiServiceError(responseBody.code ?: 404,
                        "${responseBody.message}")
                }
            } else { // Loi dac biet tu response
                error = ApiServiceError(response.code(), "${response.message()}")
            }
        } catch (e: HttpException) {
            error = ApiServiceError(response.code(), "${response.message()}")
        } catch (e: Throwable) {
            error = ApiServiceError(404, "Throwable: ${e.message}")
        }

        return PairResponse(result, error)
    }

    // Ham thuc hien cuoc goi va tra ve Response
    private suspend fun makeCall(path: String,
                                 method: Method,
                                 params: DictionaryType? = null): Response<CloudResponse> {
        // Default set Method GET
        lateinit var call: Response<CloudResponse> //= makeApiService.makeGetRequest(path)

        // Luc nay da thuc hien cuoc goi
        when (method) {
            Method.GET -> {
                call = makeApiService.makeGetRequest(path)
            }
            Method.POST -> {
                var requestBody: RequestBody? = null
                params?.let {
                    val jsonObject = JSONObject(it)
                    requestBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                }

                call = makeApiService.makePostRequest(path, requestBody)
            }
            else -> { // Note the block
                // print("x is neither 1 nor 2")
            }
        }
        return call
    }



}
