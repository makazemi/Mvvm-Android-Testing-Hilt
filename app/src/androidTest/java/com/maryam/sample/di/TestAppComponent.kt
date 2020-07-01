package com.maryam.sample.di

import android.app.Application
import com.maryam.sample.api.FakeApiService
import com.maryam.sample.repository.FakeMainRepositoryImpl
import com.maryam.sample.ui.ListPostFragmentTest
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Singleton
@Component(modules = [
    TestViewModelModule::class,
    TestAppModule::class
])
interface TestAppComponent: AppComponent {

    val apiService: FakeApiService

    val mainRepository: FakeMainRepositoryImpl

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): TestAppComponent
    }

    fun inject(sample:ListPostFragmentTest)

}














