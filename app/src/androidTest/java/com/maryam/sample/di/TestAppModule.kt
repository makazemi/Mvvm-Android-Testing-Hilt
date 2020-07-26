package com.maryam.sample.di


import android.content.Context
import androidx.room.Room
import com.maryam.sample.api.ApiService
import com.maryam.sample.api.FakeApiService
import com.maryam.sample.db.AppDatabase
import com.maryam.sample.db.PostDao
import com.maryam.sample.repository.FakeMainRepositoryImpl
import com.maryam.sample.repository.MainRepository
import com.maryam.sample.util.Constant
import com.maryam.sample.util.JsonUtil
import com.maryam.sample.util.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@InstallIn(ApplicationComponent::class)
@Module
object TestAppModule{


    @Singleton
    @Provides
    fun provideJsonUtil(): JsonUtil {
        return JsonUtil()
    }



    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext app: Context): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, Constant.DATABASE_NAME)
            .fallbackToDestructiveMigration() // get correct db version if schema changed
            .build()
    }


    @Singleton
    @Provides
    fun providePostDao(db: AppDatabase): PostDao {
        return db.getPostDao()
    }

    @Singleton
    @Provides
    fun provideMainRepository(postDao: PostDao, sessionManager: SessionManager): MainRepository {
        return FakeMainRepositoryImpl(postDao,sessionManager)
    }


    @Singleton
    @Provides
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }


    @Provides
    fun provideApiService(jsonUtil: JsonUtil): ApiService {
        return FakeApiService(jsonUtil)
    }
}














