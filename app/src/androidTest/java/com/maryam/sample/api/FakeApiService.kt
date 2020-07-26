package com.maryam.sample.api


import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maryam.sample.model.PostResponse
import com.maryam.sample.util.*
import com.maryam.sample.util.Constants.BLOG_POSTS_DATA_FILENAME
import com.maryam.sample.util.Constants.SERVER_ERROR_FILENAME
import kotlinx.coroutines.delay
import javax.inject.Inject



class FakeApiService
@Inject
constructor(
    private val jsonUtil: JsonUtil
): ApiService {

     var blogPostsJsonFileName: String = BLOG_POSTS_DATA_FILENAME
     var networkDelay: Long = 0L

    override suspend fun getPosts(token: String): GenericApiResponse<PostResponse> {
        val rawJson = jsonUtil.readJSONFromAsset(blogPostsJsonFileName)
        Log.d("FakeApiService","rawJson=$rawJson")
        val blogs = Gson().fromJson<PostResponse>(
            rawJson,
            object : TypeToken<PostResponse>() {}.type
        )
        Log.d("FakeApiService","blogs=$blogs")
        delay(networkDelay)
        if(blogPostsJsonFileName==BLOG_POSTS_DATA_FILENAME)
            return ApiSuccessResponse(blogs)
        else if(blogPostsJsonFileName==SERVER_ERROR_FILENAME)
            return ApiErrorResponse(ErrorBody(message = "SERVER ERROR"))
        else {
            Log.d("FakeApiService","apiEmptyResponse")
            return ApiEmptyResponse()
        }

    }

}