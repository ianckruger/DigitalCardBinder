package com.example.a546final

import kotlinx.coroutines.flow.Flow

class PhotoRepository(private val photoDao: PhotoDao) {
    suspend fun insertPhoto(photo: Photo) {
        photoDao.insert(photo)
    }

    fun getPhotos(): Flow<List<Photo>> {
        return photoDao.getAllPhotos()
    }
}
