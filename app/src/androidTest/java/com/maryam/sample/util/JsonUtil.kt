package com.maryam.sample.util

import android.app.Application
import java.io.IOException
import javax.inject.Inject

/**
 * Class for parsing data from fake data assets
 */
class JsonUtil
@Inject
constructor(
    private val application: Application
){

    fun readJSONFromAsset(fileName: String): String? {
        var json: String? = null
        json = try {
            val inputStream = javaClass.classLoader
                .getResourceAsStream("api-response/$fileName")
            inputStream.bufferedReader().use{it.readText()}

        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}