package com.example.a546final

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val photoDao = PhotoDatabase.getDatabase(application).photoDao()
    private val repository = PhotoRepository(photoDao)

    fun addPhotoToDatabase(photo: Photo) {
        viewModelScope.launch {
            repository.insertPhoto(photo)
        }
    }
}