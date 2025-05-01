package com.example.a546final

import kotlinx.coroutines.flow.Flow

class PhotoRepository(private val photoDao: PhotoDao) {

    val photos: Flow<List<Photo>> = photoDao.getAllPhotos()

    suspend fun addPhoto(photo: Photo) {
        photoDao.insert(photo)
    }
}