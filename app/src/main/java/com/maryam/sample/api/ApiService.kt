package com.maryam.sample.api
import com.maryam.sample.model.Post
import com.maryam.sample.util.GenericApiResponse
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface ApiService {

    @GET("posts")
    suspend fun getNotification(
    ): GenericApiResponse<List<Post>>
}