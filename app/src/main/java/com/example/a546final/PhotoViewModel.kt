package com.example.a546final

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PhotoViewModel(private val repository: PhotoRepository) : ViewModel() {
    val photos: Flow<List<Photo>> = repository.getPhotos()

    fun addPhotoToDatabase(photo: Photo) {
        viewModelScope.launch {
            repository.insertPhoto(photo)
        }
    }
}
