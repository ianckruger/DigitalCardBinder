package com.example.a546final

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val uri: String
)
