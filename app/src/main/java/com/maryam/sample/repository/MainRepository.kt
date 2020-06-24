package com.maryam.sample.repository

import androidx.lifecycle.LiveData
import com.maryam.sample.model.Post
import kotlin.coroutines.CoroutineContext

interface MainRepository {
    fun getPostsApiOnly(coroutineContext: CoroutineContext):LiveData<DataState<List<Post>>>
    fun getPostsCashOnly(coroutineContext: CoroutineContext):LiveData<DataState<List<Post>>>
    fun getPostsNetworkBoundResource(coroutineContext: CoroutineContext):LiveData<DataState<List<Post>>>
}