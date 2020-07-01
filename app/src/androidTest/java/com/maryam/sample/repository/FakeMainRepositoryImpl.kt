package com.maryam.sample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.maryam.sample.api.FakeApiService
import com.maryam.sample.db.PostDao
import com.maryam.sample.model.Post
import com.maryam.sample.model.PostResponse
import com.maryam.sample.util.CacheResponseHandler
import com.maryam.sample.util.EspressoIdlingResource
import com.maryam.sample.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


/**
 * The only difference between this and the real MainRepositoryImpl is the ApiService is
 * fake and it's not being injected so I can change it at runtime.
 * That way I can alter the FakeApiService for each individual test.
 */
@Singleton
class FakeMainRepositoryImpl @Inject
constructor() : MainRepository {

    @Inject
    lateinit var postDao: PostDao
    lateinit var apiService: FakeApiService

    private fun throwExceptionIfApiServiceNotInitialzied() {
        if (!::apiService.isInitialized) {
            throw UninitializedPropertyAccessException(
                "Did you forget to set the ApiService in FakeMainRepositoryImpl?"
            )
        }
    }

    @Throws(UninitializedPropertyAccessException::class)
    override fun getPostsApiOnly(coroutineContext: CoroutineContext): LiveData<DataState<List<Post>>> =
        wrapEspressoIdlingResource{
            liveData {
                EspressoIdlingResource.increment()
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


       }


    @Throws(UninitializedPropertyAccessException::class)
    override fun getPostsCashOnly(coroutineContext: CoroutineContext): LiveData<DataState<List<Post>>> =
      //  wrapEspressoIdlingResource {
            liveData {
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
    override fun getPostsNetworkBoundResource(coroutineContext: CoroutineContext): LiveData<DataState<List<Post>>> {
       // wrapEspressoIdlingResource {
            return object : NetworkBoundResource<PostResponse, List<Post>, List<Post>>(
                coroutineContext,
                apiCall = { apiService.getPosts() },
                cacheCall = { postDao.fetchListPost() },
                isNetworkAvailable = true
            ) {
                override suspend fun updateCache(networkObject: PostResponse) {
                    if (networkObject.posts.isNotEmpty()) {
                        withContext(Dispatchers.IO) {
                            for (item in networkObject.posts) {
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