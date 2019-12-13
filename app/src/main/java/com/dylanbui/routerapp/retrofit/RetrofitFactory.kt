package com.dylanbui.routerapp.retrofit

import com.dylanbui.routerapp.controller.user.User
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


// https://github.com/ebi-igweze/simple-call-adapter

class CloudBaseResponse<T>
{
    @SerializedName("info")
    @Expose
    val info: Info? = null

    @SerializedName("data")
    @Expose
    val data: T? = null // Getters Setters..
}

class Info {
    private val status: String? = null
    private val message: String? = null // Getters Setters..
}


class Post {
    @SerializedName("userId")
    @Expose
    var userId: Int? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null


    override fun toString(): String {
        return "Post{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
//                ", userId=" + userId +
//                ", id=" + id +
                '}'
    }
}

interface PostService {

    @GET("/posts")
    fun getCallPosts(): Call<List<Post>>

    @GET("/posts")
    fun getResponseBody(): Call<ResponseBody>


    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String?): Call<List<User?>?>?

    @POST("/make/posts")
    fun makePosts(@Body body: JsonObject): Call<List<Post>>

    @POST("/make/posts")
    fun makePostsHasMap(@Body body: HashMap<String, Any?>): Call<List<Post>>


    @POST
    fun makePostRequest(@Url url: String?,
                        @Body requestBody: RequestBody?,
                        @HeaderMap header: Map<String?, String?>?): Call<ResponseBody?>?

}


object RetrofitFactory {
    // https://jsonplaceholder.typicode.com/posts
    private const val BASE_URL = "https://jsonplaceholder.typicode.com"

    //GsonBuilder().serializeNulls().create()


    fun makeRetrofitService(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            // .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .build()//.create(RetrofitService::class.java)
    }
}