package com.maryam.sample.util

import com.maryam.sample.repository.DataState


abstract class CacheResponseHandler<ResultType, Data>(
    private val response: CacheResult<Data?>
) {
    suspend fun getResult(): DataState<ResultType> {

        return when (response) {

            is CacheResult.GenericError -> {
                DataState.error(
                    ErrorBody(message = response.errorMessage)
                )
            }

            is CacheResult.Success -> {
                handleSuccess(resultObj = response.value)
            }

        }
    }

    abstract suspend fun handleSuccess(resultObj: Data?): DataState<ResultType>

}
