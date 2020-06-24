package com.maryam.sample.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maryam.sample.model.Post

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item:Post)


    @Query("SELECT * FROM post")
    suspend fun fetchListPost():List<Post>

}