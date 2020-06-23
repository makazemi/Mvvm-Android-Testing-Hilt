package com.maryam.sample.util


class ErrorHandling{

    companion object{
        const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
        const val UNABLE_TODO_OPERATION_WO_INTERNET = "اتصال به اینترنت برقرار نمی باشد!"
        const val ERROR_CHECK_NETWORK_CONNECTION = "اتصال به اینترنت خود را چک کنید."
        const val ERROR_UNKNOWN = "خطای نامشخص"
        const val ERROR_SERVER_CONNECTION="خطا در ارتباط با سرور"
        const val NETWORK_ERROR_TIMEOUT = "Network timeout"
        const val CACHE_ERROR_TIMEOUT = "Cache timeout"
        const val NETWORK_ERROR = "خطای شبکه"


        fun isNetworkError(msg: String): Boolean = msg.contains(UNABLE_TO_RESOLVE_HOST)
    }

}