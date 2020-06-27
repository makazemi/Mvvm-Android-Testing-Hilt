package com.maryam.sample.repository

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.maryam.sample.api.FakeGithubService
import com.maryam.sample.db.AppDatabase
import com.maryam.sample.db.PostDao
import com.maryam.sample.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class MainRepositoryTest :CoroutineTestBase() {
    private lateinit var repository: MainRepository
    private  var apiService =FakeGithubService()
    private lateinit var postDao: PostDao


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        val db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext<Application>(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        postDao=db.getPostDao()

        repository=MainRepositoryImpl(apiService,postDao)
    }

    @Test
    fun getPostApiOnlyTest() {
        /** GIVEN  **/
        val postResponse= TestUtil.createPostResponse()

        /** WHEN **/
        val calledService = CompletableDeferred<Unit>()
        runBlocking {
            apiService.getPostImpl ={
                calledService.complete(Unit)
                GenericApiResponse.create(Response.success(postResponse))
            }

           repository.getPostsApiOnly(this.coroutineContext).addObserver().apply {
                calledService.await()
                advanceUntilIdle()

               /** THEN **/
                assertItems(
                   DataState.loading(true),
                    DataState.data(postResponse.posts)
                )
            }

        }

    }

}
@Test
fun getPostCashOnlyTest(){

}