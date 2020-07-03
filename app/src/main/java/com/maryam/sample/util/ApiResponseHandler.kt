package com.maryam.sample.util

import android.util.Log
import com.maryam.sample.repository.DataState
import com.maryam.sample.repository.buildError


abstract class ApiResponseHandler<ResultType, Data>(
    private val response: GenericApiResponse<Data>
) {

    private val TAG: String = "AppDebug"

    suspend fun getResult(): DataState<ResultType> {

        return when (response) {
            is ApiSuccessResponse -> {
                handleSuccess(resultObj = response.body)
            }
            is ApiErrorResponse -> {
                Log.d("FakeApiService","ApiErrorResponse=${response.error}")
                buildError(
                    response.error.message ?: ErrorHandling.ERROR_UNKNOWN,
                    response.error.code.toString()
                )

            }
            is ApiEmptyResponse -> {
                Log.d("FakeApiService","ApiEmptyResponse")
                buildError(
                    code = "204",
                    message = "HTTP 204. Returned NOTHING."
                )
            }
        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ResultType>

}