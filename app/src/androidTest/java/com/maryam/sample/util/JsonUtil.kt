package com.maryam.sample.util


import java.io.IOException
import javax.inject.Inject

/**
 * Class for parsing data from fake data assets
 */
class JsonUtil
@Inject
constructor() {

    fun readJSONFromAsset(fileName: String): String? {
        var json: String? = null
        json = try {
            val inputStream = javaClass.classLoader
                .getResourceAsStream("api-response/$fileName")
            //    Log.d("FakeApiService","buffer=${inputStream.bufferedReader().use{it.readLine()}}")
            inputStream.bufferedReader().use { it.readText() }

        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}