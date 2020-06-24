package com.maryam.sample.di
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maryam.sample.di.keys.ViewModelKey
import com.maryam.sample.ui.postList.PostListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Singleton
    @Binds
    abstract fun bindMainViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(PostListViewModel::class)
    abstract fun bindPostListViewModel(viewModel:PostListViewModel): ViewModel

}








