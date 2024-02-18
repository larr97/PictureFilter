package com.example.picturefilter.activities.editimage

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.picturefilter.activities.main.MainActivity
import com.example.picturefilter.adapters.ImageFiltersAdapter
import com.example.picturefilter.data.ImageFilter
import com.example.picturefilter.databinding.ActivityEditImageBinding
import com.example.picturefilter.listeners.ImageFilterListener
import com.example.picturefilter.utilities.displayToast
import com.example.picturefilter.utilities.show
import com.example.picturefilter.viewmodels.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity(), ImageFilterListener {

    private lateinit var binding: ActivityEditImageBinding
    private val viewModel: EditImageViewModel by viewModel()
    private lateinit var gpuImage: GPUImage

    // Image bitmaps
    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setupObservers()
        prepareImagePreview()
    }

    private fun setupObservers() {
        viewModel.imagePreviewUiState.observe(this, {
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                // For the first time 'filtered image = original image'
                originalBitmap = bitmap
                filteredBitmap. value = bitmap

                with(originalBitmap) {
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImageFilters(this)
                }
            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
        viewModel.imageFiltersUiState.observe(this, {
            val imageFiltersDataState = it ?: return@observe
            binding.imageFiltersProgressBar.visibility =
                if(imageFiltersDataState.isLoading) View.VISIBLE else View.GONE
            imageFiltersDataState.imageFilters?.let { imageFilters ->
                    ImageFiltersAdapter(imageFilters, this).also { adapter ->
                        binding.filtersRecyclerView.adapter = adapter
                    }
            } ?: kotlin.run {
            imageFiltersDataState.error?.let { error ->
                displayToast(error)
                }
            }
        })
        filteredBitmap.observe(this, { bitmap ->
            binding.imagePreview.setImageBitmap(bitmap)
        })
    }

    private fun prepareImagePreview() {
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
        viewModel.prepareImagePreview(imageUri)
        }
    }


    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter) {
            with(gpuImage) {
                setFilter(filter)
                filteredBitmap. value = bitmapWithFilterApplied
            }
        }
    }
}