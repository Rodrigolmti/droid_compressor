package com.rodrigolmti.droid_compressor.compressor.manager

import com.rodrigolmti.droid_compressor.library.entity.Image
import java.io.File
import java.text.DecimalFormat
import java.util.*

object SelectedImagesManager : SelectedImagesManagerContract {

    private const val mb = 1024 * 1024
    private const val kb = 1024

    val images: MutableList<Image> = ArrayList()

    override fun handleImage(image: Image) {
        if (!removeIfContains(image)) {
            images.add(image)
        }
    }

    override fun containsImage(image: Image): Boolean {
        for (i in images) {
            if (i.id == image.id) {
                return true
            }
        }
        return false
    }

    override fun getImagesTotalSizeFormatted(): String {
        val decimalFormat = DecimalFormat("#.##")
        var totalSize = 0.0
        for (image in images) {
            val file = File(image.path)
            if (file.exists() && file.isFile) {
                totalSize += file.length()
            }
        }
        return when {
            totalSize >= mb -> "${decimalFormat.format(totalSize / mb)} Mb"
            totalSize > kb -> "${decimalFormat.format(totalSize)} Kb"
            else -> ""
        }
    }

    override fun getImagesCount(): Int {
        return images.size
    }

    override fun getSelectedImages(): List<Image> {
        return images
    }

    private fun removeIfContains(image: Image): Boolean {
        for (img in images) {
            if (img.id == image.id) {
                images.remove(img)
                return true
            }
        }
        return false
    }

    override fun clearSelectedImages() {
        images.clear()
    }
}