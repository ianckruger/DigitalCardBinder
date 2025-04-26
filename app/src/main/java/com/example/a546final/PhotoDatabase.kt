package com.example.a546final

import androidx.room.Database


@Database(entities = [Photo::class], version = 1)
abstract class PhotoDatabase {
    abstract fun photoDao(): PhotoDao
}