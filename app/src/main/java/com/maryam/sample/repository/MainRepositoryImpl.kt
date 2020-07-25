package com.maryam.sample.repository


import com.maryam.sample.api.ApiService
import com.maryam.sample.db.PostDao
import com.maryam.sample.model.Post
import com.maryam.sample.model.PostResponse
import com.maryam.sample.util.ApiResponseHandler
import com.maryam.sample.util.CacheResponseHandler
import com.maryam.sample.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class MainRepositoryImpl @Inject constructor(private val apiService: ApiService
,private val postDao: PostDao,private val sessionManager: SessionManager):MainRepository {
    override fun getPostsApiOnly(coroutineContext: CoroutineContext): Flow<DataState<List<Post>>> =
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

    override fun getPostsCashOnly(coroutineContext: CoroutineContext): Flow<DataState<List<Post>>> =  flow {
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

    override fun getPostsNetworkBoundResource(coroutineContext: CoroutineContext): Flow<DataState<List<Post>>> {
       return object:NetworkBoundResource<PostResponse,List<Post>,List<Post>>(
           coroutineContext,
           apiCall = {apiService.getPosts()},
           cacheCall = {postDao.fetchListPost()},
           isNetworkAvailable = sessionManager.isConnectedToTheInternet()
       ){
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
    }

}