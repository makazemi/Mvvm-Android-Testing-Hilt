package com.maryam.sample.ui

import com.maryam.sample.TestBaseApplication
import com.maryam.sample.api.FakeApiService
import com.maryam.sample.di.TestAppComponent
import com.maryam.sample.repository.FakeMainRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * All tests extend this base class for easy configuration of fake Repository
 * and fake ApiService.
 */
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class BaseMainActivityTests{

    fun configureFakeApiService(
        blogsDataSource: String? = null,
        networkDelay: Long? = null,
        application: TestBaseApplication
    ): FakeApiService {
        val apiService = (application.appComponent as TestAppComponent).apiService
        blogsDataSource?.let { apiService.blogPostsJsonFileName = it }
        networkDelay?.let { apiService.networkDelay = it }
        return apiService
    }

    fun configureFakeRepository(
        apiService: FakeApiService,
        application: TestBaseApplication
    ): FakeMainRepositoryImpl {
        val mainRepository = (application.appComponent as TestAppComponent).mainRepository
        mainRepository.apiService = apiService
        return mainRepository
    }


    abstract fun injectTest(application: TestBaseApplication)
}














