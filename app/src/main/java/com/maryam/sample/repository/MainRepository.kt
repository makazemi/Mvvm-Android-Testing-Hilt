package com.maryam.sample.repository

import com.maryam.sample.model.Post
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface MainRepository {
    fun getPostsApiOnly(coroutineContext: CoroutineContext):Flow<DataState<List<Post>>>
    fun getPostsCashOnly(coroutineContext: CoroutineContext):Flow<DataState<List<Post>>>
    fun getPostsNetworkBoundResource(coroutineContext: CoroutineContext):Flow<DataState<List<Post>>>
}