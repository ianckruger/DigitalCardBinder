package com.example.a546final

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Insert
    suspend fun insert(photo: Photo)

    @Query("SELECT * FROM photo")
    fun getAllPhotos(): Flow<List<Photo>>
}
