package com.rodrigolmti.droid_compressor.library.listener

import com.rodrigolmti.droid_compressor.compressor.model.entity.Image

interface CompressLoadListener {

    fun onImageCompressed(images: List<Image>)

    fun onError()

}