package com.maryam.sample.ui.postList

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.maryam.sample.repository.MainRepository


class PostListViewModel @ViewModelInject constructor(
    private val repository: MainRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val listPostApiOnly = repository.getPostsApiOnly(viewModelScope.coroutineContext).asLiveData()

}