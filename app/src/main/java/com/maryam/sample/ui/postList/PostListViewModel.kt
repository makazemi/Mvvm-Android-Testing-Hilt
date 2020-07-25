package com.maryam.sample.ui.postList

import androidx.lifecycle.*
import com.maryam.sample.repository.MainRepository
import javax.inject.Inject

class PostListViewModel @Inject constructor(repository: MainRepository):ViewModel() {

    val listPostApiOnly=repository.getPostsApiOnly(viewModelScope.coroutineContext).asLiveData()

}