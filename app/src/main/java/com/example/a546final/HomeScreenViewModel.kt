package com.example.a546final

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: PhotoRepository
    val photos: LiveData<List<Photo>>

    init {

    }

    fun addPhoto() {

    }

    fun deletePhoto() {

    }
}