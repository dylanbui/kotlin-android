package com.dylanbui.android_library.placesautocomplete

import android.location.Location
import android.location.LocationManager
import com.google.gson.JsonObject

data class Address(val longName: String, val shortName: String, val type: ArrayList<String>)

data class GgPlace(var jsonData: JsonObject) {

    private val rawData: JsonObject = jsonData

    public var placeId: String = ""
    get() {
        return rawData.getAsJsonObject("place_id").asString
    }

    public var name: String = ""
        get() {
            var terms = rawData.getAsJsonArray("terms")
            if (terms != null) {
                return terms[0].asJsonObject.getAsJsonObject("value").asString
            }
            return "noname"
        }

    public var descriptionPlace: String = ""
        get() {
            return rawData.getAsJsonObject("description").asString
        }

    public var mainAddress: String = ""
        get() {
            return rawData.getAsJsonObject("structured_formatting").getAsJsonObject("main_text").asString
        }

    public var secondaryAddress: String = ""
        get() {
            return rawData.getAsJsonObject("structured_formatting").getAsJsonObject("secondary_text").asString
        }


    override fun toString(): String {
        return rawData.toString()
    }
}

data class GgPlaceDetail(var jsonData: JsonObject) {

    private val rawData: JsonObject = jsonData

    public var placeId: String = ""
        get() {
            return rawData.get("place_id").asString
            Sai toan bo roi, chinh lai
            // return rawData.getAsJsonObject("place_id").asString
        }

    public var name: String = ""
        get() {
            return rawData.getAsJsonObject("name").asString
        }

    public var formattedAddress: String = ""
        get() {
            return rawData.getAsJsonObject("formatted_address").asString
        }

    public var addressComponents: JsonObject = JsonObject()
        get() {
            return rawData.getAsJsonObject("address_components")
        }


    public var location: Location = Location("")
        get() {
            // Mac dinh la 0 => bay ra bien
            var location: Location = Location(LocationManager.GPS_PROVIDER)
            location.latitude = rawData.getAsJsonObject("geometry")
                .getAsJsonObject("location")
                .getAsJsonObject("lat").asDouble
            location.longitude = rawData.getAsJsonObject("geometry")
                .getAsJsonObject("location")
                .getAsJsonObject("lng").asDouble
            return location
        }

    override fun toString(): String {
        return rawData.toString()
    }
}


data class Place(val id: String, val description: String) {
    override fun toString(): String {
        return ""
    }
}

data class PlaceDetails(
    val id: String,
    val name: String,
    val address: ArrayList<Address>,
    val lat: Double,
    val lng: Double,
    val placeId: String,
    val url: String,
    val utcOffset: Int,
    val vicinity: String,
    val compoundPlusCode: String,
    val globalPlusCode: String
)