package com.example.picturefilter.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.picturefilter.data.ImageFilter
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorMatrixFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import java.io.InputStream

class EditImageRepositoryImpl(private val context: Context): EditImageRepository {

    override suspend fun getImageFilters(image: Bitmap): List<ImageFilter> {
        val gpuImage = GPUImage(context).apply {
            setImage(image)
        }
        val imageFilters: ArrayList<ImageFilter> = ArrayList()

        //region:: Image Filters

        // Normal
        GPUImageFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Normal" ,
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Retro
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.2f, 0.0f,
                0.1f, 0.1f, 1.0f, 0.0f,
                1.9f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Retro",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Just
        GPUImageColorMatrixFilter(
            0.9f,
            floatArrayOf(
                0.4f, 0.6f, 0.5f, 0.0f,
                0.0f, 0.4f, 1.0f, 0.0f,
                0.05f, 0.1f, 0.4f, 0.4f,
                1.0f, 1.0f, 1.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Just",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Hume
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.25f, 0.0f, 0.2f, 0.0f,
                0.0f, 1.0f, 0.2f, 0.0f,
                0.0f, 0.3f, 1.0f, 0.3f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Hume",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Desert
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.6f, 0.4f, 0.2f, 0.05f,
                0.0f, 0.8f, 0.3f, 0.05f,
                0.3f, 0.3f, 0.5f, 0.08f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Desert",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Old Times
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.05f, 0.0f, 0.0f,
                -0.2f, 1.1f, -0.2f, 0.11f,
                0.2f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Old Times",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        // Red
        val targetRed = 0.8f
        val targetGreen = 0.0f
        val targetBlue = 0.0f
        val targetAlpha = 1.0f

        val targetColor = floatArrayOf(targetRed, targetGreen, targetBlue, targetAlpha)

        val colorMatrix = floatArrayOf(
            targetColor[0], 0.0f, 0.0f, 0.0f,
            0.0f, targetColor[1], 0.0f, 0.0f,
            0.0f, 0.0f, targetColor[2], 0.0f,
            0.0f, 0.0f, 0.0f, targetColor[3]
        )

        GPUImageColorMatrixFilter(1.0f, colorMatrix).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Red",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

        //endregion

        return imageFilters
    }

    override suspend fun prepareImagePreview(imageUri: Uri): Bitmap? {
        getInputStreamFromUri(imageUri)?.let { inputStream ->
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val width = context.resources.displayMetrics.widthPixels
            val height = ((originalBitmap.height * width) / originalBitmap.width)
            return Bitmap.createScaledBitmap(originalBitmap, width, height, false)
        } ?: return null
    }

    private fun getInputStreamFromUri(uri: Uri): InputStream? {
        return context. contentResolver.openInputStream(uri)
    }
}