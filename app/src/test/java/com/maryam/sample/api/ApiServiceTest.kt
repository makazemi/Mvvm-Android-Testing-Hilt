package com.maryam.sample.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.maryam.sample.model.PostResponse
import com.maryam.sample.util.ApiResponseCallAdapterFactory
import com.maryam.sample.util.ApiSuccessResponse
import com.maryam.sample.util.Constant.SECRET_KEY
import com.maryam.sample.util.LiveDataCallAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class ApiServiceTest {


    // Executes each task synchronously using Architecture Components.
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: ApiService

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getUser() {
        enqueueResponse("posts.json")
        val response = runBlocking {
            (service.getPosts(SECRET_KEY) as ApiSuccessResponse).body
        }
        val request = mockWebServer.takeRequest()
        assertThat(request.path, `is`("5ef30832e2ce6e3b2c78907b"))

        assertThat<PostResponse>(response, notNullValue())
        val post=response.posts[0]
        assertThat(post.id , `is`(1))
        assertThat(post.userId , `is`(1))
        assertThat(post.title , `is`("sunt aut facere repellat provident occaecati excepturi optio reprehenderit"))
        assertThat(post.body , `is`("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"))

    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader?.getResourceAsStream("api-response/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}