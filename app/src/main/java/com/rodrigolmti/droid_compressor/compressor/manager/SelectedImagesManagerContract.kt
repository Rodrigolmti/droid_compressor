package com.rodrigolmti.droid_compressor.compressor.manager

import com.rodrigolmti.droid_compressor.compressor.model.entity.Image

interface SelectedImagesManagerContract {

    fun handleImage(image: Image)

    fun containsImage(image: Image): Boolean

    fun getImagesCount(): Int

    fun getImagesTotalSizeFormatted(): String

    fun getSelectedImages() : List<Image>

    fun clearSelectedImages()
}