package com.rodrigolmti.droid_compressor.library.listener

import com.rodrigolmti.droid_compressor.library.entity.Image

interface ImageLoaderListener {

    fun onImageLoaded(folders: List<Image>)

}