package com.example.picturefilter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.picturefilter.data.ImageFilter
import com.example.picturefilter.databinding.ItemContainerFilterBinding
import com.example.picturefilter.listeners.ImageFilterListener

class ImageFiltersAdapter(
    private val imageFilters: List<ImageFilter>,
    private val imageFilterListener: ImageFilterListener
    ) : RecyclerView.Adapter<ImageFiltersAdapter. ImageFilterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        val binding = ItemContainerFilterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageFilterViewHolder, position: Int) {
        with(holder) {
            with(imageFilters[position]) {
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.textFilterName.text = name
                binding.root.setOnClickListener {
                    imageFilterListener.onFilterSelected(this)
                }
            }
        }
    }

    override fun getItemCount() = imageFilters.size

    inner class ImageFilterViewHolder(val binding: ItemContainerFilterBinding) :
        RecyclerView.ViewHolder(binding.root)
}