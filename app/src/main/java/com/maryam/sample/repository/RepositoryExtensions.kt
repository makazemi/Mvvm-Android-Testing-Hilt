package com.maryam.sample.repository


import android.util.Log
import com.maryam.sample.util.ApiErrorResponse
import com.maryam.sample.util.CacheResult
import com.maryam.sample.util.Constant.CACHE_TIMEOUT
import com.maryam.sample.util.Constant.NETWORK_TIMEOUT
import com.maryam.sample.util.ErrorBody
import com.maryam.sample.util.ErrorHandling.Companion.CACHE_ERROR_TIMEOUT
import com.maryam.sample.util.ErrorHandling.Companion.ERROR_CHECK_NETWORK_CONNECTION
import com.maryam.sample.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.maryam.sample.util.ErrorHandling.Companion.NETWORK_ERROR
import com.maryam.sample.util.ErrorHandling.Companion.UNABLE_TODO_OPERATION_WO_INTERNET
import com.maryam.sample.util.GenericApiResponse
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.CoroutineContext

/**
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */
private val TAG: String = "NetworkBoundResource"


suspend fun <T> safeApiCall(
    isNetworkAvailable: Boolean,
    coroutineContext: CoroutineContext=Dispatchers.IO,
    apiCall: suspend () -> GenericApiResponse<T>

): GenericApiResponse<T> {
    if (!isNetworkAvailable) {
        return ApiErrorResponse(ErrorBody(message = UNABLE_TODO_OPERATION_WO_INTERNET))
    }
    return withContext(coroutineContext) {
        try {
            withTimeout(NETWORK_TIMEOUT) {
                Log.d(TAG, "Try")
                apiCall.invoke()
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code = "408" // timeout error code
                    Log.d(TAG, "TimeoutCancellationException=${throwable.message}")
                    ApiErrorResponse<T>(ErrorBody(code, ERROR_CHECK_NETWORK_CONNECTION))
                }
                is IOException -> {
                    Log.d(TAG, "IOException")
                    ApiErrorResponse<T>(ErrorBody(message = NETWORK_ERROR))
                }
                is HttpException -> {
                    val code = throwable.code().toString()
                    val errorResponse =
                        ErrorBody.convertToObject(convertErrorBody(throwable)).userMessage
                    Log.d(TAG, "HttpException=${throwable.message()}")
                    ApiErrorResponse<T>(ErrorBody(code, errorResponse))
                }
                else -> {
                    Log.d(TAG, "else")
                    ApiErrorResponse<T>(ErrorBody(message = ERROR_UNKNOWN))

                }
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    coroutineContext: CoroutineContext,
    cacheCall: suspend () -> T?
): CacheResult<T?> {
    return  withContext(coroutineContext) {
    try {
        // throws TimeoutCancellationException
        withTimeout(CACHE_TIMEOUT) {
            CacheResult.Success(cacheCall.invoke())
        }
    } catch (throwable: Throwable) {
        Log.d(TAG, "safeCacheCall call=${cacheCall.toString()} and throwable=${throwable.message}")
        when (throwable) {
            is TimeoutCancellationException -> {
                CacheResult.GenericError(CACHE_ERROR_TIMEOUT)
            }
            else -> {
                CacheResult.GenericError(ERROR_UNKNOWN)
            }

        }
    }
}

}


fun <ResultType> buildError(
    message: String,
    code: String? = null
): DataState<ResultType> {
    return DataState.error(
        ErrorBody(code, message)
    )


}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        ERROR_UNKNOWN
    }
}























