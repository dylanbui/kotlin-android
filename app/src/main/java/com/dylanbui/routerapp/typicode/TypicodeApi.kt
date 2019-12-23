package com.dylanbui.routerapp.typicode

import android.util.Log
import com.dylanbui.routerapp.networking.*
import com.dylanbui.routerapp.utils.fromJson
import com.dylanbui.routerapp.utils.guardLet
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException
import java.net.URLEncoder

object TyPostApi {

    fun getPostSyn(callback: (List<TyPost>, AppNetworkServiceError?) -> Unit) {

        var strUrl = "posts"
        Log.d("TAG", "String Url : $strUrl")

        TypicodeApi.getSynch(strUrl, object : TypicodeApiCallback {
            override fun success(responseData: JsonElement) {
                callback(fromJson(responseData), null)
            }

            override fun failure(error: AppNetworkServiceError) {
                // Empty list
                callback(listOf<TyPost>(), error)
            }
        })
    }

    fun getPost(callback: (List<TyPost>, AppNetworkServiceError?) -> Unit) {

        var strUrl = "posts"
        Log.d("TAG", "String Url : $strUrl")

        TypicodeApi.get(strUrl, object : TypicodeApiCallback {
            override fun success(responseData: JsonElement) {
                callback(fromJson(responseData), null)
            }

            override fun failure(error: AppNetworkServiceError) {
                // Empty list
                callback(listOf<TyPost>(), error)
            }
        })
    }

    fun getPostDetail(postId: Int = 0, callback: (TyPost, AppNetworkServiceError?) -> Unit) {

        var strUrl = "posts/${postId}"
        Log.d("TAG", "String Url : $strUrl")

        TypicodeApi.get(strUrl, object : TypicodeApiCallback {
            override fun success(responseData: JsonElement) {
                callback(fromJson(responseData), null)
            }

            override fun failure(error: AppNetworkServiceError) {
                // Empty list
                callback(TyPost(), error)
            }
        })
    }

}

object TyCommentApi {

    fun getCommentByPostId(postId: Int, callback: (List<TyComment>, AppNetworkServiceError?) -> Unit) {

        var strUrl = "posts/${postId}/comments"
        Log.d("TAG", "String Url : $strUrl")

        TypicodeApi.get(strUrl, object : TypicodeApiCallback {
            override fun success(responseData: JsonElement) {
                callback(fromJson(responseData), null)
            }

            override fun failure(error: AppNetworkServiceError) {
                // Empty list
                callback(listOf<TyComment>(), error)
            }
        })
    }

}

object TyPhotoApi {

    fun getPhotoSyn(page: Int = 0, callback: (List<TyPhoto>, AppNetworkServiceError?) -> Unit) {

        val offset = 20
        var start = page * offset
        var strUrl = "photos?_start=${start}&_limit=${offset}"
        Log.d("TAG", "String Url : $strUrl")

        TypicodeApi.getSynch(strUrl, object : TypicodeApiCallback {
            override fun success(responseData: JsonElement) {
                callback(fromJson(responseData), null)
            }

            override fun failure(error: AppNetworkServiceError) {
                // Empty list
                callback(listOf<TyPhoto>(), error)
            }
        })
    }

    fun getPhoto(page: Int = 0, callback: (List<TyPhoto>, AppNetworkServiceError?) -> Unit) {

        val offset = 20
        var start = page * offset
        var strUrl = "photos?_start=${start}&_limit=${offset}"
        Log.d("TAG", "String Url : $strUrl")

        TypicodeApi.get(strUrl, object : TypicodeApiCallback {
            override fun success(responseData: JsonElement) {
                callback(fromJson(responseData), null)
            }

            override fun failure(error: AppNetworkServiceError) {
                // Empty list
                callback(listOf<TyPhoto>(), error)
            }
        })
    }

    fun getDetailPhoto(photoId: Int = 0, callback: (TyPhoto, AppNetworkServiceError?) -> Unit) {

        var strUrl = "photos/${photoId}"
        Log.d("TAG", "String Url : $strUrl")

        TypicodeApi.get(strUrl, object : TypicodeApiCallback {
            override fun success(responseData: JsonElement) {
                callback(fromJson(responseData), null)
            }

            override fun failure(error: AppNetworkServiceError) {
                // Empty list
                callback(TyPhoto(), error)
            }
        })
    }

}


object TyUserApi {

    fun getUser(callback: (List<TyUser>, AppNetworkServiceError?) -> Unit) {
        TypicodeApi.get("users", object : TypicodeApiCallback {
            override fun success(responseData: JsonElement) {
//                var list: List<TyUser> = fromJson(responseData)
//                callback(list, null)
                callback(fromJson(responseData), null)
            }

            override fun failure(error: AppNetworkServiceError) {
                // Empty list
                callback(listOf<TyUser>(), error)
            }
        })
    }

}

interface TypicodeApiCallback {
    fun success(responseData: JsonElement)
    fun failure(error: AppNetworkServiceError)
}

private interface TypicodeApiService {

    // @Headers("Content-Type: application/json")
    @GET
    fun makeGetRequest(@Url url: String?): Call<JsonElement>

    @POST
    fun makePostRequest(@Url url: String?,
                        @Body requestBody: RequestBody?): Call<JsonElement>

    @Multipart // don use with json
    @POST()
    fun makeUploadFileRequest(@Url url: String?,
                              @PartMap() partMap: HashMap<String, JsonElement>?,
                              @Part file: MultipartBody.Part): Call<JsonElement>

}


object TypicodeApi {

    enum class Method(val method: String) {
        GET("GET"),
        POST("POST"),
    }

    private var BASE_URL = "https://jsonplaceholder.typicode.com/"

    private val makeApiService: TypicodeApiService by lazy {
        Log.d("WebAccess", "Creating retrofit client")

        // -- Log for Retrofit --
        var httpLoggingInterceptor = HttpLoggingInterceptor()
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        // Create Retrofit client
        return@lazy retrofit.create(TypicodeApiService::class.java)
    }

    // Set at Application()
    fun setBaseUrl(str: String) {
        BASE_URL = str
    }

    private fun convertToJsonObject(dict: DictionaryType): JsonObject {
        var jsonObject = JsonObject()

        dict.forEach {
            // New ways
            when (it.value) {
                null -> jsonObject.add(it.key, JsonNull.INSTANCE)
                is Boolean -> jsonObject.addProperty(it.key, it.value as Boolean)
                is Number -> jsonObject.addProperty(it.key, it.value as Number)
                else -> jsonObject.addProperty(it.key, it.value.toString())
            }
        }
        return jsonObject
    }

    private fun convertToUrlEncode(map: DictionaryType): String {
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

    fun getWithSuspend(path: String) {
        // Default set Method GET
        // var call: Call<JsonElement> = makeApiService.makeGetRequest(path)
    }



    fun get(path: String, callback: TypicodeApiCallback) {
        // Default set Method GET
        var call: Call<JsonElement> = makeApiService.makeGetRequest(path)

        call.enqueue(
            object: Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.code() == 200) {
                        response.body()?.let {
                            // Xu ly cac loi cua CloudResponse tra ve tu server, goi ham neu can
                            // onFailed(MyNetworkingServiceError("123", "khong loi"))
                            // onResponse(it)

                            //Thread.sleep(2000)

                            callback.success(it)
                            return
                        }
                    }
                    // Co loi, tra ve loi o day
                    callback.failure(AppNetworkServiceError(response.code().toString(), response.message()))
                }
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    // Day la loi, xu ly loi o day
                    // onFailed?.let { it(AppNetworkServiceError("404", t.message ?: "Unknown")) }
                    callback.failure(AppNetworkServiceError("404", t.message ?: "Unknown"))
                }
            })

    }

    fun getSynch(path: String, callback: TypicodeApiCallback) {

        var call: Call<JsonElement> = makeApiService.makeGetRequest(path)
        // Run in the same thread
        try {
            // call and handle response
            var response: Response<JsonElement> = call.execute()
            if (response.code() == 200) {
                var (body) = guardLet(response.body()) {
                    // Co loi, tra ve loi o day
                    callback.failure(AppNetworkServiceError(response.code().toString(), response.message()))
                    return
                }
                // -- Successfully
                callback.success(body)

            } else {
                callback.failure(AppNetworkServiceError(response.code().toString(), response.message()))


            }
        } catch (t: IOException) {
            callback.failure(AppNetworkServiceError("404", t.message ?: "Catch IOException"))
        }

    }
}