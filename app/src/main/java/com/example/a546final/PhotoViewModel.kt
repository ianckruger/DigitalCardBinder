package com.example.a546final

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PhotoViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: PhotoRepository
    val photos: LiveData<List<Photo>>

    init {
        val dao = PhotoDatabase.getDatabase(application).photoDao()
        repo = PhotoRepository(dao)
        photos = repo.photos.asLiveData()
    }

    fun addPhotoToDatabase(photo: Photo) = viewModelScope.launch {
        repo.insertPhoto(photo)
    }
}