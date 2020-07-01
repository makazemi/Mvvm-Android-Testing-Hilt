package com.maryam.sample.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maryam.sample.repository.FakeMainRepositoryImpl
import com.maryam.sample.ui.postList.PostListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class FakeMainViewModelFactory
@Inject
constructor(
    private val mainRepository: FakeMainRepositoryImpl
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostListViewModel::class.java)) {
            return PostListViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}















