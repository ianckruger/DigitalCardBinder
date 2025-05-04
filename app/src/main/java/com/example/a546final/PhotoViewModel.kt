package com.example.a546final

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PhotoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PhotoRepository
    val photos: LiveData<List<Photo>>

    init {
        val photoDao = PhotoDatabase.getDatabase(application).photoDao()
        repository = PhotoRepository(photoDao)
        photos = repository.allPhotos
    }

    fun insert(photo: Photo) = viewModelScope.launch {
        repository.insert(photo)
    }
    fun deletePhoto(photo: Photo) = viewModelScope.launch{
        repository.deletePhoto(photo)
    }

    fun updatePhotoName(photo: Photo) = viewModelScope.launch {
        repository.updatePhoto(photo)
    }
}