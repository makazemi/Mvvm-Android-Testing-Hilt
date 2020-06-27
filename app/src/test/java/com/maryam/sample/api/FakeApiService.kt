package com.maryam.sample.api

import com.maryam.sample.model.PostResponse
import com.maryam.sample.util.GenericApiResponse

/**
 * Fake API implementation that does not implement anything.
 * Designed for tests to fake
 */
open class FakeGithubService(
     var getPostImpl: suspend (token: String) -> GenericApiResponse<PostResponse> = notImplemented1()
) :ApiService {
    companion object {
        private fun <T, R> notImplemented1(): suspend (t: T) -> R {
            return { t: T ->
                TODO("")
            }
        }

        private fun <T, R> notImplemented2(): suspend (t: T) -> R {
            return { t: T ->
                TODO("")
            }
        }
    }

    override suspend fun getPosts(token: String): GenericApiResponse<PostResponse> = getPostImpl(token)
}