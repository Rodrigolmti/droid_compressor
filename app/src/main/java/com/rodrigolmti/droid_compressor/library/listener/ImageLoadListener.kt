package com.rodrigolmti.droid_compressor.library.listener

import com.rodrigolmti.droid_compressor.compressor.model.entity.Image

interface ImageLoaderListener {

    fun onImageLoaded(images: List<Image>)

}