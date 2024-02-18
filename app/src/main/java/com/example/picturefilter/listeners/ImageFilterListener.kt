package com.example.picturefilter.listeners

import com.example.picturefilter.data.ImageFilter

interface ImageFilterListener {
    fun onFilterSelected(imageFilter: ImageFilter)
}