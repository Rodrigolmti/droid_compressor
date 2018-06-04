package com.rodrigolmti.droid_compressor.library.listener

import com.rodrigolmti.droid_compressor.library.entity.Image

interface CompressLoadListener {

    fun onImageCompressed(images: List<Image>)

    fun onError()

}