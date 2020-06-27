package com.maryam.sample.di

import android.app.Application
import androidx.room.Room
import com.maryam.sample.api.ApiService
import com.maryam.sample.db.AppDatabase
import com.maryam.sample.db.PostDao
import com.maryam.sample.repository.MainRepository
import com.maryam.sample.repository.MainRepositoryImpl
import com.maryam.sample.util.ApiResponseCallAdapterFactory
import com.maryam.sample.util.Constant.BASE_URL
import com.maryam.sample.util.Constant.DATABASE_NAME
import com.maryam.sample.util.LiveDataCallAdapterFactory
import com.maryam.sample.util.SessionManager
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

    @JvmStatic
    @Singleton
    @Provides
    fun provideAppDb(app: Application): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration() // get correct db version if schema changed
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun providePostDao(db: AppDatabase): PostDao {
        return db.getPostDao()
    }
    @JvmStatic
    @Singleton
    @Provides
    fun provideMainRepository(apiService: ApiService,postDao: PostDao,sessionManager: SessionManager): MainRepository {
        return MainRepositoryImpl(apiService,postDao,sessionManager)
    }


}