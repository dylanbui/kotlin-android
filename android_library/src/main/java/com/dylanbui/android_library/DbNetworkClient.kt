package com.dylanbui.android_library

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

private val TIMEOUT = 10*1000

object DbNetworkClient {

    init {
        // println(DbNetworkClient: Singleton class invoked.")
    }

    fun doRequest(url: String, method: String = "GET",
                  params: JsonObject = JsonObject(),
                  complete: ((JsonElement, String?) -> Unit)? = null): Pair<JsonElement, String?> {

        if (complete != null) {
            Thread(Runnable {
                var responsePair = this._doRequest(url, method, params)
                complete(responsePair.first, responsePair.second)
            }).start()
            return Pair(JsonObject(), null)
        }

        return this._doRequest(url, method, params)

//        var responseStr = this._doRequest(url, method, params)
//        if (responseStr == null) {
//            complete(JsonObject(), "ERROR")
//            return
//        }

//        var responseStr = this._doRequest(url, method, params).guard {
//            complete(JsonObject(), "ERROR")
//            return
//        }
//        var jsonElement = JsonParser().parse(responseStr)
//        complete(jsonElement, null)
    }

    private fun _doRequest(strUrl: String, method: String = "GET", params: JsonObject = JsonObject()): Pair<JsonElement, String?> {
        val url = URL(strUrl)
        var httpClient = url.openConnection() as HttpURLConnection
        if (url.protocol == "https") {
            httpClient = url.openConnection() as HttpsURLConnection
        }
        httpClient.readTimeout = TIMEOUT
        httpClient.connectTimeout = TIMEOUT
        httpClient.requestMethod = method

        if (method == "POST") {
            httpClient.instanceFollowRedirects = false
            httpClient.doOutput = true
            httpClient.doInput = true
            httpClient.useCaches = false
            httpClient.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        }
        try {
            if (method == "POST") {
                httpClient.connect()
                val os = httpClient.outputStream
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(params.toString())
                writer.flush()
                writer.close()
                os.close()
            }

            if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = BufferedInputStream(httpClient.inputStream)
                val responseStr: String = readStream(inputStream = stream)
                return Pair(JsonParser().parse(responseStr), null)
            } else {
                println("ERROR ${httpClient.responseCode}")
                return Pair(JsonObject(), "ERROR ${httpClient.responseCode}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            httpClient.disconnect()
        }

        return Pair(JsonObject(), "ERROR: Unknown")
    }

    private fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }
}