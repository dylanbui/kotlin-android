package com.dylanbui.routerapp.networking

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.dylanbui.routerapp.utils.guardLet
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.BufferedSink
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.URLEncoder


class AppNetworkServiceError(var errorCode: String, var errorMessage: String)

typealias onResponseSuccess = (responseBody: CloudResponse) -> Unit?
typealias onResponseFailed = (errorData: AppNetworkServiceError) -> Unit?
typealias onProgressCallback = (progress: Float) -> Unit

typealias DictionaryType = HashMap<String, Any?>

// Default response from server
class CloudResponse {
    @SerializedName("result")
    @Expose
    var result: Boolean? = false

    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("validatedMessage")
    @Expose
    var validatedMessage: String? = null


    @SerializedName("data")
    @Expose
    var data: JsonElement? = null // Phai la JsonElement (co the chua JsonObject, JsonArray, JsonNULL) khong de Any (Kotlin)
}

interface AppNetworkApiService {

    // @Headers("Content-Type: application/json")
    @GET
    fun makeGetRequest(@Url url: String?): Call<CloudResponse>

    @POST
    fun makePostRequest(@Url url: String?,
                        @Body requestBody: RequestBody?): Call<CloudResponse>

    // @Multipart don use with json
    @POST()
    fun makeUploadFileRequest(@Url url: String?,
                              @Body requestBody: RequestBody?,
                              @Part file: MultipartBody.Part): Call<CloudResponse>

    // Old
    @POST()
    fun postJson(@Body body: HashMap<String, Any?>?): Call<CloudResponse>

    @PUT
    fun makePutRequest(@Url url: String?,
                       @Body requestBody: RequestBody?): Call<CloudResponse>

    @PUT
    fun makePutRequests(@Url url: String?,
                       @Body requestBody: RequestBody?): Single<CloudResponse>


    // At more PATH, PUT, UPLOAD
}

object AppNetwork {

    enum class Method(val method: String) {
        GET("GET"),
        POST("POST"),
    }

    private var BASE_URL = "http://45.117.162.60:8080/diy/api/"
    private lateinit var retrofit: Retrofit

    // Set at Application()
    fun setBaseUrl(str: String) {
        // -- Log for Retrofit --
        var httpLoggingInterceptor = HttpLoggingInterceptor()
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        BASE_URL = str
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()

        retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    }

    fun convertToJsonObject(dict: DictionaryType): JsonObject {
        var jsonObject = JsonObject()

        dict.forEach {
            // New ways
            when (it.value) {
                null -> jsonObject.add(it.key, JsonNull.INSTANCE)
                is Boolean -> jsonObject.addProperty(it.key, it.value as Boolean)
                is Number -> jsonObject.addProperty(it.key, it.value as Number)
                else -> jsonObject.addProperty(it.key, it.value.toString())
            }
//            if (it.value == null) {
//                jsonObject.add(it.key, JsonNull.INSTANCE)
//            } else {
//                if (it.value is Boolean) {
//                    jsonObject.addProperty(it.key, it.value as Boolean)
//                } else if (it.value is Number) {
//                    jsonObject.addProperty(it.key, it.value as Number)
//                } else {
//                    jsonObject.addProperty(it.key, it.value.toString())
//                }
//            }
        }
        return jsonObject
    }

    fun convertToUrlEncode(map: DictionaryType): String {
        var sb = StringBuilder()
        map.entries.forEach {
            if (it.value == null) {
                sb.append(it.key).append('=').append('&')
            } else {
                sb.append(it.key)
                    .append('=')
                    .append(URLEncoder.encode(it.value.toString(), "UTF_8"))
                    .append('&')
            }
        }
        sb.delete(sb.length - 1, sb.length)
        return sb.toString()
    }

    fun review() {
//        var requestBody: RequestBody = MultipartBody.Builer()
//        requestBody.addFormDataPart("title", "Square Logo")
        //Uri.Builder

        URLEncoder.encode("", "UTF-8")
        // URLEncodedUtils.format("", Charset.forName("UTF-8"));

        val text = "plain text request body"
        // val body: RequestBody = RequestBody().create(MediaType.parse("text/plain"), text)

        var dict = DictionaryType()
        dict["so_1"] = null
        dict["so_2"] = "Gia tri 2"
        dict["so_3"] = 33

        val jsonObject = convertToJsonObject(dict)

        val stringUrl = convertToUrlEncode(dict)
        Log.d("Name", stringUrl)

//        val jsonObject = JsonObject()
//        jsonObject.addProperty("name", "Ancd test")
//        jsonObject.addProperty("city", "delhi")
//        jsonObject.addProperty("age", "23")
//        jsonObject.add("giatri_null", JsonNull.INSTANCE)

        val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        Log.d("Name", body.toString())



//        val file = File("path")
//        file.asRequestBody("image/jpeg".toMediaTypeOrNull())


//        var part = MultipartBody.Part.createFormData("1", "")
//        MultipartBody.Part.createFormData("3", "")
//        MultipartBody.Part.createFormData("4", "")


        // -- Call upload file --
//        val fileUpload = File(fileUrl)
//        val fileUploadService = RetrofitClientInstance.retrofitInstance.create(FileUploadService::class.java)
//        val requestBody = file.asRequestBody(file.extension.toMediaTypeOrNull())
//        val filePart = MultipartBody.Part.createFormData(
//            "blob", fileUpload.name, requestBody
//        )
//        val call = fileUploadService.uploadFile(filePart)


    }

    fun doRequest(path: String, method: Method, params: DictionaryType? = null,
                  onResponse: onResponseSuccess,
                  onFailed: onResponseFailed? = null) {
        // Default set Method GET
        var call: Call<CloudResponse> = makeApiService().makeGetRequest(path)

        when (method) {
            Method.GET -> {
                call = makeApiService().makeGetRequest(path)
            }
            Method.POST -> {
                var requestBody: RequestBody? = null
                params?.let {
                    val jsonObject = convertToJsonObject(it)
                    requestBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                }

                call = makeApiService().makePostRequest(path, requestBody)
            }
            else -> { // Note the block
                // print("x is neither 1 nor 2")
            }
        }

        doAsyncCall(call, onResponse, onFailed)
    }

    fun doUploadFile(path: String,
                     fileField: String,
                     fileUpload: File,
                     onResponse: onResponseSuccess,
                     onProgress: onProgressCallback,
                     onFailed: onResponseFailed) {

        // create RequestBody instance from file
        val requestFile = RequestBodyWithProgress(fileUpload, RequestBodyWithProgress.ContentType.JPG_IMAGE, onProgress)

        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData(fileField, fileUpload.name, requestFile)

        var call: Call<CloudResponse> = makeApiService().makeUploadFileRequest(path, null, body)

        doAsyncCall(call, onResponse, onFailed)
    }

    // Asynchronously
    private fun doAsyncCall(call: Call<CloudResponse>, onResponse: onResponseSuccess, onFailed: onResponseFailed?) {
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
                    onFailed?.let { it(AppNetworkServiceError("404", t.message ?: "Unknown")) }
                }
            })
    }

    // Synchronously
    private fun doExecuteCall(call: Call<CloudResponse>, onResponse: onResponseSuccess, onFailed: onResponseFailed?) {
        /**
         * Synchronously send the request and return its response.
         *
         * @throws IOException if a problem occurred talking to the server.
         * @throws RuntimeException (and subclasses) if an unexpected error occurs creating the request
         * or decoding the response.
         */
//        @Throws(IOException::class)
//        fun execute(): Response<T?>?

        // Run in the same thread
        try {
            // call and handle response
            var response: Response<CloudResponse> = call.execute()
            if (response.code() == 200) {
                var (body) = guardLet(response.body()) {
                    onFailed?.let {
                        onFailed(AppNetworkServiceError("404", "response.body() is NULL"))
                    }
                    return
                }
                // -- Successfully
                onResponse(body)

                // Su dung cach 2
//                response.body()?.let { it
//                    // -- Successfully
//                    onResponse(it)
//                    return
//                }
//                onFailed(AppNetworkServiceError("404", "response.body() is NULL"))

                // Kiem tra thu
//                var body = response.body().guard {
//                    onFailed(AppNetworkServiceError("404", t.message ?: "response.body() is NULL"))
//                    return
//                }
//                var body = response.body()?.let { it } ?: return

            } else {
                onFailed?.let {
                    it(AppNetworkServiceError(response.code().toString(), response.message() ?: "response.message() is NULL"))
                }

//                if (response?.code() in 400..511)
//                handler(null, HttpException(response))
//                else
//                handler(response?.body(), null)
            }
        } catch (t: IOException) {
            onFailed?.let {
                it(AppNetworkServiceError("404", t.message ?: "Catch IOException"))
            }
        }
    }

    private fun makeApiService(): AppNetworkApiService {
        return retrofit.create(AppNetworkApiService::class.java)
    }



}

class RequestBodyWithProgress(
    private val file: File,
    private val contentType: ContentType,
    private val progressCallback:((progress: Float)->Unit)?) : RequestBody() {

    override fun contentType(): MediaType? = contentType.toString().toMediaTypeOrNull()
    // MediaType.parse(contentType.description)

    override fun contentLength(): Long = file.length()

    override fun writeTo(sink: BufferedSink) {
        val fileLength = contentLength()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val inSt = FileInputStream(file)
        var uploaded = 0L
        inSt.use {
            var read: Int = inSt.read(buffer)
            //val handler = Http2Reader.Handler(Looper.getMainLooper())
            val handler = Handler(Looper.getMainLooper())
            while (read != -1) {
                progressCallback?.let {
                    uploaded += read
                    val progress = (uploaded.toDouble() / fileLength.toDouble()).toFloat()
                    handler.post { it(progress) }

                    sink.write(buffer, 0, read)
                }
                read = inSt.read(buffer)
            }
        }
    }

    enum class ContentType(val description: String) {
        PNG_IMAGE("image/png"),
        JPG_IMAGE("image/jpg"),
        IMAGE("image/*")
    }
}