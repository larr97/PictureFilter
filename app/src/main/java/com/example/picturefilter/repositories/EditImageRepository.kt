package com.example.picturefilter.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.example.picturefilter.data.ImageFilter

interface EditImageRepository {
    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?
    suspend fun getImageFilters(image: Bitmap): List<ImageFilter>
}