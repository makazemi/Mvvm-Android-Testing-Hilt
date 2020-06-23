package com.maryam.sample.di
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Singleton
    @Binds
    abstract fun bindMainViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


//    @Binds
//    @IntoMap
//    @ViewModelKey(CameraViewModel::class)
//    abstract fun bindCameraViewModel(viewModel:CameraViewModel): ViewModel

}








