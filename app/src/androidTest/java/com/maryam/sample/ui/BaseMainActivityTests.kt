package com.maryam.sample.ui

import com.maryam.sample.api.FakeApiService
import com.maryam.sample.db.PostDao
import com.maryam.sample.repository.FakeMainRepositoryImpl
import com.maryam.sample.util.JsonUtil
import com.maryam.sample.util.SessionManager
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

/**
 * All tests extend this base class for easy configuration of fake Repository
 * and fake ApiService.
 */
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class BaseMainActivityTests{

//@Inject
//lateinit var jsonUtil: JsonUtil
//
//
//    @Inject
//   lateinit var apiService: FakeApiService
//
//    @Inject
//    lateinit var mainRepository: FakeMainRepositoryImpl
//
//
//
//    fun configureFakeApiService(
//        blogsDataSource: String? = null,
//        networkDelay: Long? = null
//    ): FakeApiService {
//        blogsDataSource?.let { apiService.blogPostsJsonFileName = it }
//        networkDelay?.let { apiService.networkDelay = it }
//        return apiService
//    }
//
//    fun configureFakeRepository(
//        apiService: FakeApiService
//    ): FakeMainRepositoryImpl {
//        mainRepository.apiService = apiService
//        return mainRepository
//    }



}














