package com.example.a546final

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.launch

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val photoDatabase = Room.databaseBuilder(
        application,
        PhotoDatabase::class.java,
        "photo_database"
    ).build()

    private val repo: PhotoRepository = PhotoRepository(photoDatabase.photoDao())

    fun addPhotoToDatabase(photo: Photo) {
        viewModelScope.launch {
            repo.addPhoto(photo)
        }
    }

    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            // Add delete logic here if needed
        }
    }
}
