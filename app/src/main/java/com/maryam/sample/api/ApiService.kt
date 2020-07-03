package com.maryam.sample.api
import com.maryam.sample.model.PostResponse
import com.maryam.sample.util.Constant.SECRET_KEY
import com.maryam.sample.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import javax.inject.Singleton

@Singleton
interface ApiService {
   @GET("5ed788d079382f568bd24eed/1") /*success list*/
   // @GET("5ed788d079382f568bd24eed/2") /*empty list*/
    suspend fun getPosts(
        @Header("secret-key") token: String=SECRET_KEY
    ): GenericApiResponse<PostResponse>
}