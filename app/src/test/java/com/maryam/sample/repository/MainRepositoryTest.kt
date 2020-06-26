package com.maryam.sample.repository

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.maryam.sample.api.ApiService
import com.maryam.sample.api.FakeGithubService
import com.maryam.sample.db.AppDatabase
import com.maryam.sample.db.PostDao
import com.maryam.sample.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineContext
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class MainRepositoryTest {
    private lateinit var repository: MainRepository
    private val apiService: ApiService = FakeGithubService()
    private lateinit var postDao: PostDao

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

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
    fun testRepo() {
        val list=repository.getPostsApiOnly(mainCoroutineRule.coroutineContext).getOrAwaitValue()

        assertThat(list.data?.getContentIfNotHandled(),(not(nullValue())))
        assertThat(list.data?.getContentIfNotHandled()?.get(2)?.title,`is`("test title 3"))
    }
}