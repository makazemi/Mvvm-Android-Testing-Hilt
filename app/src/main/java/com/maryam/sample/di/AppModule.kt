package com.maryam.sample.di

import com.maryam.sample.api.ApiService
import com.maryam.sample.util.ApiResponseCallAdapterFactory
import com.maryam.sample.util.Constant.BASE_URL
import com.maryam.sample.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object AppModule{


    @JvmStatic
    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .build()
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit
            .create(ApiService::class.java)
    }

//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideMainRepository(app: Application,
//                               apiService: ApiService):MainRepository{
//        return MainRepository(app,apiService)
//    }


}