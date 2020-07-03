package com.maryam.sample.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Post(
    @PrimaryKey
    val id: Int,
    val imagePath: String,
    val title: String
//    var id: Int,
//    var userId: Int,
//    var title: String,
//    var body: String
):Parcelable