package com.dylanbui.routerapp.networking

import android.os.AsyncTask
import com.dylanbui.routerapp.utils.fromJson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

// -- [String] <=> ['URL', 'Method: GET, POST', 'Json String for POST'] --
// AsyncTask<String, Void, String>
// val 1 : Array String bo vao khi goi HttpTask().execute("string", "string")
// val 2 : Gia tri tra ve cho ham onProgressUpdate(Integer... progress), Void la ko dung ham nay
// val 3 : Ket qua tra ve cua Http, thuong la String Json

class HttpTask(callback: (JsonElement?) -> Unit): AsyncTask<String, Void, String>()  {

    var callback = callback

    private val TIMEOUT = 10*1000
    // -- [String] <=> ['URL', 'Method: GET, POST', 'Json String for POST'] --
    override fun doInBackground(vararg params: String?): String {
        val url = URL(params[0])
        var httpClient = url.openConnection() as HttpURLConnection
        if (url.protocol == "https") {
            httpClient = url.openConnection() as HttpsURLConnection
        }
        httpClient.readTimeout = TIMEOUT
        httpClient.connectTimeout = TIMEOUT
        httpClient.requestMethod = params[1]

        if (params[1] == "POST") {
            httpClient.instanceFollowRedirects = false
            httpClient.doOutput = true
            httpClient.doInput = true
            httpClient.useCaches = false
            httpClient.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        }
        try {
            if (params[1] == "POST") {
                httpClient.connect()
                val os = httpClient.outputStream
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(params[2])
                writer.flush()
                writer.close()
                os.close()
            }
            if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = BufferedInputStream(httpClient.inputStream)
                return readStream(inputStream = stream)
//                val data: String = readStream(inputStream = stream)
//                return data
            } else {
                println("ERROR ${httpClient.responseCode}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            httpClient.disconnect()
        }
        return "ERROR" // Json string empty
    }

    private fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        if (result == "ERROR") {
            callback(null)
        } else {
            callback(JsonParser().parse(result))
        }
    }


}

