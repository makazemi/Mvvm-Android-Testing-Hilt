package com.maryam.sample.api
import com.maryam.sample.model.PostResponse
import com.maryam.sample.util.Constant.SECRET_KEY
import com.maryam.sample.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import javax.inject.Singleton

@Singleton
interface ApiService {
    @GET("5ef30832e2ce6e3b2c78907b")
    suspend fun getPosts(
        @Header("secret-key") token: String=SECRET_KEY
    ): GenericApiResponse<PostResponse>
}