package com.rodrigolmti.droid_compressor.library.utils.runnables

import android.content.Context
import android.provider.MediaStore
import com.rodrigolmti.droid_compressor.library.entity.Folder
import com.rodrigolmti.droid_compressor.library.entity.Image
import com.rodrigolmti.droid_compressor.library.listener.FolderLoaderListener
import com.rodrigolmti.droid_compressor.library.listener.ImageLoaderListener
import java.util.ArrayList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FileLoader internal constructor(private val context: Context) {

    private val imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private var executorService: ExecutorService? = null

    fun loadDeviceImages(folder: Folder, listener: ImageLoaderListener) {
        getExecutorService()?.execute(ImageLoadRunnable(folder, listener))
    }

    fun loadDeviceFolders(listener: FolderLoaderListener) {
        getExecutorService()?.execute(FolderLoadRunnable(listener))
    }

    fun abortLoadImages() {
        if (executorService != null) {
            executorService!!.shutdown()
            executorService = null
        }
    }

    private fun getExecutorService(): ExecutorService? {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor()
        }
        return executorService
    }

    private inner class FolderLoadRunnable(private val listener: FolderLoaderListener) : Runnable {

        override fun run() {
            listener.onFolderLoaded(getFolder())
        }
    }

    private inner class ImageLoadRunnable internal constructor(private val folder: Folder, private val listener: ImageLoaderListener) : Runnable {

        override fun run() {
            listener.onImageLoaded(getImagesByFolder(folder.bucketId))
        }
    }

    private fun getFolder(): List<Folder> {
        val folders = ArrayList<Folder>()
        try {

            val projection = arrayOf("Distinct " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID)
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(imagesUri,
                    projection,
                    "", null,
                    ""
            )
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    folders.add(Folder.fromCursor(cursor))

                } while (cursor.moveToNext())
                cursor.close()
            }

        } catch (error: Exception) {
            error.printStackTrace()
        }
        return folders
    }

    private fun getImagesByFolder(bucketId: String): List<Image> {
        val images = ArrayList<Image>()
        try {

            val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DATA)
            val cursor = MediaStore.Images.Media
                    .query(context.contentResolver, imagesUri, projection,
                            MediaStore.Images.Media.BUCKET_ID + " = ?", arrayOf(bucketId),
                            MediaStore.Images.Media.DATE_ADDED + " DESC")
            if (cursor.moveToFirst()) {
                do {

                    images.add(Image.fromCursor(cursor))

                } while (cursor.moveToNext())
            }
            cursor.close()

        } catch (error: Exception) {
            error.printStackTrace()
        }

        return images
    }
}
