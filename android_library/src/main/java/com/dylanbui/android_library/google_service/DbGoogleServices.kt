package com.dylanbui.android_library.google_service

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.text.TextUtils
import android.util.Log
import com.dylanbui.android_library.DbNetworkClient
import com.dylanbui.android_library.R
import com.dylanbui.android_library.utils.dLog
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.net.URLEncoder

data class GgAddress(val longName: String, val shortName: String, val type: ArrayList<String>)

data class GgPlace(var jsonData: JsonObject) {

    private val rawData: JsonObject = jsonData

    public var placeId: String = ""
        get() {
            return rawData.get("place_id").asString
        }

    public var name: String = ""
        get() {
            var terms = rawData.getAsJsonArray("terms")
            if (terms != null) {
                return terms[0].asJsonObject.get("value").asString
            }
            return "noName"
        }

    public var descriptionPlace: String = ""
        get() {
            return rawData.get("description").asString
        }

    public var mainAddress: String = ""
        get() {
            return rawData.getAsJsonObject("structured_formatting").get("main_text").asString
        }

    public var secondaryAddress: String = ""
        get() {
            return rawData.getAsJsonObject("structured_formatting").get("secondary_text").asString
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
        }

    public var name: String = ""
        get() {
            return rawData.get("name").asString
        }

    public var formattedAddress: String = ""
        get() {
            return rawData.get("formatted_address").asString
        }

    public var location: Location = Location("")
        get() {
            // Mac dinh la 0 => bay ra bien
            var location: Location = Location(LocationManager.GPS_PROVIDER)
            location.latitude = rawData.getAsJsonObject("geometry")
                .getAsJsonObject("location")
                .get("lat").asDouble
            location.longitude = rawData.getAsJsonObject("geometry")
                .getAsJsonObject("location")
                .get("lng").asDouble
            return location
        }

    public var addressComponents: ArrayList<GgAddress> = ArrayList<GgAddress>()
        get() {
            var components = rawData.getAsJsonArray("address_components")
            val addressList = ArrayList<GgAddress>()
            getAddress(components, addressList)
            return addressList
        }

    private fun getAddress(addressArray: JsonArray, address: ArrayList<GgAddress>) {
        (0 until addressArray.count()).forEach { i ->
            val addressObject = addressArray[i].asJsonObject
            val addressTypeArray = addressObject.get("types").asJsonArray
            val addressType = ArrayList<String>()
            parseAddressType(addressTypeArray, addressType, address, addressObject)
        }
    }

    private fun parseAddressType(
        addressTypeArray: JsonArray, addressType: ArrayList<String>,
        address: ArrayList<GgAddress>, addressObject: JsonObject
    ) {
        (0 until addressTypeArray.count()).forEach { j ->
            addressType.add(
                addressTypeArray[j].asString // .getString(j)
            )
        }
        address.add(
            GgAddress(
                addressObject.get("long_name").asString,
                addressObject.get("short_name").asString,
                addressType
            )
        )
    }


    override fun toString(): String {
        return rawData.toString()
    }
}

class DbGoogleServicesException(message: String?) : Exception(message)

class DbGoogleServices private constructor(
    var apiKey: String?, var sessionToken: String?, var appContext: Context
) {

    /**
     * Used to get details for the places api to be showed in the auto complete list
     */
    fun requestPlaces(input: String, asyncTask: Boolean = true, complete: (ArrayList<GgPlace>?) -> Unit) {
        checkInitialization()

        val sb = buildApiUrl("https://maps.googleapis.com/maps/api/place/autocomplete/json")
        sb.append("&input=" + URLEncoder.encode(input, "utf8"))
        sb.append("&types=")
        sb.append("&components=country:VN")
        dLog(sb.toString())

        var processResponse: (Pair<JsonElement, String?>) -> Unit = {
            var resultList: ArrayList<GgPlace>? = null
            if (it.second == null) {
                var jsonObject = it.first as JsonObject
                var predictions = jsonObject.get("predictions").asJsonArray
                resultList = ArrayList<GgPlace>(predictions.count())
                for (i in 0 until predictions.count()) {
                    resultList.add(GgPlace(predictions[i].asJsonObject))
                }
            } else {
                dLog(it.second ?: "Error Unknown")
            }
            complete(resultList)
        }

        if (asyncTask ) {
            Thread(Runnable {
                var responsePair =
                    DbNetworkClient.doRequest(sb.toString())
                processResponse(responsePair)
            }).start()
            return
        }

        var responsePair =
            DbNetworkClient.doRequest(sb.toString())
        processResponse(responsePair)
    }

    fun requestPlaceDetail(placeId: String, completion: (GgPlaceDetail?) -> Unit) {
        checkInitialization()

        val sb = buildApiUrl("https://maps.googleapis.com/maps/api/place/details/json")
        sb.append("&placeid=$placeId")
        sb.append("&components=country:VN")

        DbNetworkClient.doRequest(
            sb.toString(),
            method = "GET",
            complete = { jsonElement, str ->
                if (str != null) {
                    dLog(str)
                    completion(null)
                    return@doRequest
                }
                // results is Object
                var result = (jsonElement as JsonObject).get("result").asJsonObject
                completion(GgPlaceDetail(result))
            })
    }

    fun retrieveAddressInfoFromAddress(strAddress: String, completion: ((GgPlaceDetail?) -> Unit)) {
        checkInitialization()

        val sb = buildApiUrl("https://maps.googleapis.com/maps/api/geocode/json")
        sb.append("&address=$strAddress")

        DbNetworkClient.doRequest(
            sb.toString(),
            method = "GET",
            complete = { jsonElement, str ->
                if (str != null) {
                    dLog(str)
                    completion(null)
                    return@doRequest
                }
                // results is Array
                var results = (jsonElement as JsonObject).get("results").asJsonArray
                completion(GgPlaceDetail(results[0].asJsonObject))
            })
    }

    fun retrieveAddressInfoFromLocation(location: Location, completion: ((GgPlaceDetail?) -> Void)) {
        checkInitialization()

        val sb = buildApiUrl("https://maps.googleapis.com/maps/api/geocode/json")
        sb.append("&latlng=${location.latitude},${location.longitude}")

        DbNetworkClient.doRequest(
            sb.toString(),
            method = "GET",
            complete = { jsonElement, str ->
                if (str != null) {
                    dLog(str)
                    completion(null)
                    return@doRequest
                }
                // results is Array
                var results = (jsonElement as JsonObject).get("results").asJsonArray
                completion(GgPlaceDetail(results[0].asJsonObject))
            })
    }

    private fun checkInitialization() {
        if (TextUtils.isEmpty(apiKey)) {
            throw DbGoogleServicesException(
                appContext.getString(R.string.error_lib_not_initialized)
            )
        }
    }

    private fun buildApiUrl(apiUrl: String): StringBuilder {
        val sb = StringBuilder(apiUrl)
        sb.append("?key=$apiKey")
        if (!TextUtils.isEmpty(sessionToken)) {
            sb.append("&sessiontoken=$sessionToken")
        }
        return sb
    }

    private fun logError(e: Exception, resource: Int) {
        Log.e(DbGoogleServices::class.java.simpleName, appContext.getString(resource), e)
    }



    /**
     * The data class used as builder to allow the user to use different configs of the places API
     */
    data class Builder(
        private var apiKey: String? = null,
        private var sessionToken: String? = null
    ) {
        /**
         * Sets the api key for the PlaceAPI
         */
        fun apiKey(apiKey: String) = apply { this.apiKey = apiKey }

        /**
         * Sets a unique session token for billing in the PlaceAPI
         */
        fun sessionToken(sessionToken: String) = apply { this.sessionToken = sessionToken }

        /**
         * Builds and creates an object of the PlaceAPI
         */
        fun build(context: Context) =
            DbGoogleServices(
                apiKey,
                sessionToken,
                context
            )
    }
}