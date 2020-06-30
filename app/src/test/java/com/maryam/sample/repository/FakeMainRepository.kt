package com.maryam.sample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.maryam.sample.model.Post
import com.maryam.sample.util.ErrorBody
import com.maryam.sample.util.TestUtil
import kotlinx.coroutines.delay
import kotlin.coroutines.CoroutineContext

class FakeMainRepository: MainRepository {

    private var shouldReturnError = false

    private var posts:List<Post> =TestUtil.createListPost()

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }
    override fun getPostsApiOnly(coroutineContext: CoroutineContext): LiveData<DataState<List<Post>>> = liveData {
        if(shouldReturnError)
            emit( DataState.error<List<Post>>(ErrorBody(message = "ERROR IN TEST")))
        else{
            emit(DataState.loading(true))
            delay(1000)
            emit(DataState.data(posts))
        }
    }

    override fun getPostsCashOnly(coroutineContext: CoroutineContext): LiveData<DataState<List<Post>>>  = liveData {
        if(shouldReturnError)
            emit( DataState.error<List<Post>>(ErrorBody(message = "ERROR IN TEST")))
        else{
            emit(DataState.loading(true))
            emit(DataState.data(posts))
        }
    }

    override fun getPostsNetworkBoundResource(coroutineContext: CoroutineContext): LiveData<DataState<List<Post>>>  = liveData {
        if(shouldReturnError)
            emit( DataState.error<List<Post>>(ErrorBody(message = "ERROR IN TEST")))
        else{
            emit(DataState.loading(true))
            emit(DataState.data(posts))
        }
    }

    fun addPosts(list:List<Post>){
        posts=list
    }
}