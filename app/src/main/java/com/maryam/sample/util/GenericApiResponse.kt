package com.maryam.sample.util

import android.util.Log
import com.maryam.sample.util.ErrorHandling.Companion.ERROR_SERVER_CONNECTION
import retrofit2.Response

/**
 * Copied from Architecture components google sample:
 * https://github.com/googlesamples/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/api/ApiResponse.kt
 */
@Suppress("unused") // T is used in extending classes
sealed class GenericApiResponse<T> {

    companion object {

        const val TAG="NetworkBoundResource"

        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            Log.d(TAG,"GenericApiResponse create=${error.message}")
            return ApiErrorResponse(
                ErrorBody( message = error.message ?: "unknown error")
            )
        }

        fun <T> create(response: Response<T>): GenericApiResponse<T> {
            if(response.isSuccessful){
                val body = response.body()
                return if (body == null || response.code()==204) {
                    ApiEmptyResponse()
                } else if(response.code() == 401){
                    ApiErrorResponse( ErrorBody(response.code().toString(),"401 Unauthorized. Token may be invalid."))
                } else {
                    ApiSuccessResponse(body = body)
                }
            }
            else{

                val msg = response.errorBody()?.string()
                val userMessage= ErrorBody.convertToObject(msg).userMessage
                Log.d(TAG,"else response error=$msg")
                val errorMsg = if (userMessage.isEmpty()) {
                    ERROR_SERVER_CONNECTION
                } else {
                    userMessage
                }
                return ApiErrorResponse(
                    ErrorBody(response.code().toString(),errorMsg)
                )
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : GenericApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : GenericApiResponse<T>()

data class ApiErrorResponse<T>(val error:ErrorBody) : GenericApiResponse<T>()

