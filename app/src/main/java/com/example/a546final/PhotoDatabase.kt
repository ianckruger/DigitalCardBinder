package com.example.a546final

import androidx.room.*

@Database(entities = [Photo::class], version = 1)
abstract class PhotoDatabase : RoomDatabase() { // Extend RoomDatabase here
    abstract fun photoDao(): PhotoDao
}