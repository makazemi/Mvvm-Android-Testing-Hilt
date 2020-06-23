package com.maryam.sample.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maryam.sample.model.Post


@Database(entities = [Post::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getPostDao(): PostDao

}








