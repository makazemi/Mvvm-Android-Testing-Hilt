package com.maryam.sample.ui.postList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maryam.sample.repository.MainRepository
import javax.inject.Inject

class PostListViewModel @Inject constructor(repository: MainRepository):ViewModel() {

    val listPost=repository.getPostsApiOnly(viewModelScope.coroutineContext)
}