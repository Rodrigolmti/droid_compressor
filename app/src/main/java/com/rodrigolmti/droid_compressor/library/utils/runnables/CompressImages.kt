package com.rodrigolmti.droid_compressor.library.utils.runnables

import android.content.Context
import android.net.Uri
import com.rodrigolmti.droid_compressor.compressor.manager.SelectedImagesManager
import com.rodrigolmti.droid_compressor.library.entity.Image
import com.rodrigolmti.droid_compressor.library.listener.CompressLoadListener
import com.rodrigolmti.droid_compressor.library.utils.BitmapHelper
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CompressImages internal constructor(private val context: Context) {

    private var executorService: ExecutorService? = null

    fun compressSelectedImages(listener: CompressLoadListener) {
        getExecutorService()?.execute(CompressImageRunnable(listener))
    }

    private fun getExecutorService(): ExecutorService? {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor()
        }
        return executorService
    }

    private inner class CompressImageRunnable internal constructor(private val listener: CompressLoadListener) : Runnable {

        override fun run() {
            compressImages(listener)
        }
    }

    private fun compressImages(listener: CompressLoadListener) {
        try {
            val compressedImages: MutableList<Image> = ArrayList()
            for (image in SelectedImagesManager.images) {
                compressedImages.add(Image(image.id, image.name, BitmapHelper.compressImage(context,
                        Uri.parse(image.path).path), Date()))
            }
            listener.onImageCompressed(compressedImages)
        } catch (error: Exception) {
            listener.onError()
            error.stackTrace
        }
    }
}