package com.maryam.sample.repository

import com.maryam.sample.api.FakeApiService
import com.maryam.sample.db.PostDao
import com.maryam.sample.model.Post
import com.maryam.sample.model.PostResponse
import com.maryam.sample.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


import kotlin.coroutines.CoroutineContext


/**
 * The only difference between this and the real MainRepositoryImpl is the ApiService is
 * fake and it's not being injected so I can change it at runtime.
 * That way I can alter the FakeApiService for each individual test.
 */

class FakeMainRepositoryImpl
 @Inject constructor(private val postDao: PostDao,private val sessionManager: SessionManager) : MainRepository {

    lateinit var apiService: FakeApiService


    private fun throwExceptionIfApiServiceNotInitialzied() {
        if (!::apiService.isInitialized) {
            throw UninitializedPropertyAccessException(
                "Did you forget to set the ApiService in FakeMainRepositoryImpl?"
            )
        }
    }

    @Throws(UninitializedPropertyAccessException::class)
    override fun getPostsApiOnly(coroutineContext: CoroutineContext): Flow<DataState<List<Post>>> =
        wrapEspressoIdlingResource{
        flow {
            emit(DataState.loading(true))
            val apiResult = safeApiCall(sessionManager.isConnectedToTheInternet(),coroutineContext) {
                apiService.getPosts()
            }

            emit(
                object : ApiResponseHandler<List<Post>, PostResponse>(
                    response = apiResult
                ) {
                    override suspend fun handleSuccess(resultObj: PostResponse): DataState<List<Post>> {
                        return DataState.data(resultObj.items)
                    }


                }.getResult()
            )
        }

    }


    @Throws(UninitializedPropertyAccessException::class)
    override fun getPostsCashOnly(coroutineContext: CoroutineContext): Flow<DataState<List<Post>>> =
      //  wrapEspressoIdlingResource {
            flow {
                val apiResult = safeCacheCall(coroutineContext) {
                    postDao.fetchListPost()
                }
                emit(
                    object : CacheResponseHandler<List<Post>, List<Post>>(
                        response = apiResult
                    ) {
                        override suspend fun handleSuccess(resultObj: List<Post>?): DataState<List<Post>> {
                            return DataState.data(resultObj)
                        }

                    }.getResult()
                )

            }
     //   }


    @Throws(UninitializedPropertyAccessException::class)
    override fun getPostsNetworkBoundResource(coroutineContext: CoroutineContext): Flow<DataState<List<Post>>> {
       // wrapEspressoIdlingResource {
            return object : NetworkBoundResource<PostResponse, List<Post>, List<Post>>(
                coroutineContext,
                apiCall = { apiService.getPosts() },
                cacheCall = { postDao.fetchListPost() },
                isNetworkAvailable = true
            ) {
                override suspend fun updateCache(networkObject: PostResponse) {
                    if (networkObject.items.isNotEmpty()) {
                        withContext(Dispatchers.IO) {
                            for (item in networkObject.items) {
                                try {
                                    launch {
                                        postDao.insert(item)
                                    }
                                } catch (e: Exception) {
                                }
                            }
                        }
                    }
                }

                override fun handleCacheSuccess(resultObj: List<Post>?): DataState<List<Post>> {
                    return DataState.data(resultObj)
                }

                override fun shouldFetch(cacheObject: List<Post>?): Boolean = true

            }.result
       // }

    }


}