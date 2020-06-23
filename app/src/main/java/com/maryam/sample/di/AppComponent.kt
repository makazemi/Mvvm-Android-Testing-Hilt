package com.maryam.sample.di

import android.app.Application
import com.maryam.sample.base.BaseActivity
import com.maryam.sample.ui.MainActivity
import com.maryam.sample.ui.postDetail.DetailPostFragment
import com.maryam.sample.ui.postList.ListPostFragment

import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(activity:BaseActivity)
    fun inject(activity:MainActivity)
    fun inject(fragment: ListPostFragment)
    fun inject(fragment: DetailPostFragment)
}








