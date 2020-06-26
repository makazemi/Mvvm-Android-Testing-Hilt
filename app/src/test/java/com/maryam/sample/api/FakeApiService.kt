package com.maryam.sample.api

import com.maryam.sample.model.Post
import com.maryam.sample.model.PostResponse
import com.maryam.sample.util.ApiSuccessResponse
import com.maryam.sample.util.GenericApiResponse

/**
 * Fake API implementation that does not implement anything.
 * Designed for tests to fake
 */
open class FakeGithubService() : ApiService {
    override suspend fun getPosts(token: String): GenericApiResponse<PostResponse> {
        val list=ArrayList<Post>()
        list.add(Post(1,1,"test title 1","test body body 1"))
        list.add(Post(2,2,"test title 2","test body body 2"))
        list.add(Post(3,3,"test title 3","test body body 3"))
        list.add(Post(4,4,"test title 4","test body body 4"))
        list.add(Post(5,5,"test title 5","test body body 5"))
        val postResponse= PostResponse(list)
        return ApiSuccessResponse(postResponse)
    }
}