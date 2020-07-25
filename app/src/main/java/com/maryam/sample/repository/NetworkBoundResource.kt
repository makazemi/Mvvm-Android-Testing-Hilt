package com.maryam.sample.repository

import android.util.Log
import com.maryam.sample.util.*
import com.maryam.sample.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.maryam.sample.util.ErrorHandling.Companion.UNABLE_TODO_OPERATION_WO_INTERNET
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.CoroutineContext


abstract class NetworkBoundResource<NetworkObj, CacheObj, ResultType>
constructor(
    private val dispatcher: CoroutineContext,
    private val apiCall: suspend () -> GenericApiResponse<NetworkObj>,
    private val cacheCall: suspend () -> CacheObj?,
    isNetworkAvailable: Boolean
) {
    private val TAG: String = "NetworkBoundResource"

    val result: Flow<DataState<ResultType>> = flow{

        emit(DataState.loading<ResultType>(true, cachedData = null))

        val willFetch = shouldFetch(cacheCall.invoke())
        if (!willFetch) {
            emit(returnCache())

        } else {
            if (isNetworkAvailable) {
                when (val apiResult = safeApiCall(isNetworkAvailable,dispatcher){apiCall.invoke()}) {
                    is ApiSuccessResponse -> {
                        updateCache(apiResult.body)

                    }
                    is ApiErrorResponse -> {
                        emit(
                            buildError<ResultType>(
                                apiResult.error.message ?: ERROR_UNKNOWN,
                                apiResult.error.code.toString()
                            )
                        )
                    }
                    is ApiEmptyResponse -> {
                        emit(
                            buildError<ResultType>(
                                code = "204",
                                message = "HTTP 204. Returned NOTHING."
                            )
                        )
                    }
                }
                emit(returnCache())

            } else {
                Log.d(TAG,"Not NetworkAvailable")
                emit(
                    buildError<ResultType>(
                        message = UNABLE_TODO_OPERATION_WO_INTERNET
                    )
                )
            }
        }


    }

    private suspend fun returnCache(): DataState<ResultType> {

        val cacheResult = safeCacheCall(dispatcher){cacheCall.invoke()}


        return object : CacheResponseHandler<ResultType, CacheObj>(
            response = cacheResult
        ) {
            override suspend fun handleSuccess(resultObj: CacheObj?): DataState<ResultType> {
                return handleCacheSuccess(resultObj)
            }
        }.getResult()

    }

    abstract suspend fun updateCache(networkObject: NetworkObj)

    abstract fun handleCacheSuccess(resultObj: CacheObj?): DataState<ResultType>
    abstract fun shouldFetch(cacheObject: CacheObj?): Boolean

}

