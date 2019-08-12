package com.pear2pear.car2car.restcalls.resources

import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

abstract class Resource {
    companion object {
        const val BASE_ADDRESS: String = "http://207.154.207.31"
    }

    fun request(method: String, arg: JSONObject? = null, token: String? = null): JSONObject {
        val url = URL(getURL())
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = method

        connection.setRequestProperty("Content-Type", "application/json; utf-18")
        if(token != null){
            connection.setRequestProperty("Authorization", token)
        }

        if(arg != null){
            connection.doOutput = true
            val outStream = connection.outputStream
            outStream.use {
                val message = arg.toString().toByteArray()
                outStream.write(message)
            }
        }

        if (connection.responseCode != 201 && connection.responseCode != 200) {
            throw IOException(String.format("Failed Connection - error %s", connection.responseCode))
        }

        val readBuffer = BufferedReader(InputStreamReader(connection.inputStream))
        var response = ""
        readBuffer.use {
            response = readBuffer.readLines().joinToString(separator = "")
        }
        return JSONObject(response)
    }

    abstract fun getURL(): String
}