package com.rodrigolmti.droid_compressor.library.utils.runnables

import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.rodrigolmti.droid_compressor.library.entity.Folder
import com.rodrigolmti.droid_compressor.library.entity.Image
import com.rodrigolmti.droid_compressor.library.listener.FolderLoaderListener
import com.rodrigolmti.droid_compressor.library.listener.ImageLoaderListener
import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FileLoader internal constructor(private val context: Context) {

    private val imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private var executorService: ExecutorService? = null

    fun loadDeviceImages(folder: Folder?, listener: ImageLoaderListener) {
        getExecutorService()?.execute(ImageLoadRunnable(folder, listener))
    }

    fun loadDeviceFolders(listener: FolderLoaderListener) {
        getExecutorService()?.execute(FolderLoadRunnable(listener))
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

    private inner class ImageLoadRunnable internal constructor(private val folder: Folder?, private val listener: ImageLoaderListener) : Runnable {

        override fun run() {
            listener.onImageLoaded(getImagesByFolder(folder?.bucketId ?: ""))
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
            error.stackTrace
        }
        return folders
    }

    private fun getImagesByFolder(bucketId: String): List<Image> {
        val images = ArrayList<Image>()
        try {

            if (bucketId.isEmpty()) {
                val folder = File(Environment.getExternalStorageDirectory().path + "/Pictures/DroidCompressor")
                folder.mkdirs()
                val files = folder.listFiles { _, name -> name?.endsWith(".jpg")!! || name.endsWith(".jpeg") || name.endsWith(".png") }
                if (files.isNotEmpty()) {
                    for (file in files) {
                        if (file.isFile && file.exists()) {
                            images.add(Image(0L, file.name, file.path, Date(file.lastModified())))
                        }
                    }
                    images.sortedWith(compareBy({ it.id }, { it.date }))
                }
            } else {
                val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DATA)
                val cursor = MediaStore.Images.Media.query(context.contentResolver, imagesUri, projection,
                        MediaStore.Images.Media.BUCKET_ID + " = ?", arrayOf(bucketId),
                        MediaStore.Images.Media.DATE_ADDED + " DESC")
                if (cursor.moveToFirst()) {
                    do {

                        images.add(Image.fromCursor(cursor))

                    } while (cursor.moveToNext())
                }
                cursor.close()
            }

        } catch (error: Exception) {
            error.stackTrace
        }
        return images
    }
}
