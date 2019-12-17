package com.dylanbui.routerapp.typicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TyUser {

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null


    override fun toString(): String {
        return "User {" +
                ", id = " + id +
                "name ='" + name + '\'' +
                ", email ='" + email + '\'' +
                '}'
    }
}

class TyPhoto {

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("albumId")
    @Expose
    var albumId: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null


    @SerializedName("thumbnailUrl")
    @Expose
    var thumbnailUrl: String? = null

    override fun toString(): String {
        return "Photo {" +
                ", id = " + id +
                "title ='" + title + '\'' +
                '}'
    }
}