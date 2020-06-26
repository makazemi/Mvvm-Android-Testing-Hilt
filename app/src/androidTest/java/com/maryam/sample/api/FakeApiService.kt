package com.maryam.sample.api


import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maryam.sample.model.PostResponse
import com.maryam.sample.util.*
import com.maryam.sample.util.Constants.BLOG_POSTS_DATA_FILENAME
import com.maryam.sample.util.Constants.SERVER_ERROR_FILENAME
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeApiService
@Inject
constructor(
    private val jsonUtil: JsonUtil
): ApiService {

    private var blogPostsJsonFileName: String = Constants.BLOG_POSTS_DATA_FILENAME
    private var networkDelay: Long = 0L

    override suspend fun getPosts(token: String): GenericApiResponse<PostResponse> {
        val rawJson = jsonUtil.readJSONFromAsset(blogPostsJsonFileName)
        val blogs = Gson().fromJson<PostResponse>(
            rawJson,
            object : TypeToken<PostResponse>() {}.type
        )
        delay(networkDelay)
        if(blogPostsJsonFileName==BLOG_POSTS_DATA_FILENAME)
            return ApiSuccessResponse(blogs)
        else if(blogPostsJsonFileName==SERVER_ERROR_FILENAME)
            return ApiErrorResponse(ErrorBody(message = "SERVER ERROR"))
        else
            return ApiEmptyResponse()
    }

}