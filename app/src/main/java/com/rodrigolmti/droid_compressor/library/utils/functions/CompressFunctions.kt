package com.rodrigolmti.droid_compressor.library.utils.functions

import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

fun compressImage(context: Context, imageUri: String): String {

    val filePath = getRealPathFromURI(context, imageUri)
    var scaledBitmap: Bitmap? = null

    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    var bmp = BitmapFactory.decodeFile(filePath, options)

    var actualHeight = options.outHeight
    var actualWidth = options.outWidth

    val maxHeight = 816.0f
    val maxWidth = 1024.0f
    var imgRatio = (actualWidth / actualHeight).toFloat()
    val maxRatio = maxWidth / maxHeight

    if (actualHeight > maxHeight || actualWidth > maxWidth) {
        when {
            imgRatio < maxRatio -> {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            }
            imgRatio > maxRatio -> {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            }
            else -> {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }
    }

    options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
    options.inJustDecodeBounds = false
    options.inPurgeable = true
    options.inInputShareable = true
    options.inTempStorage = ByteArray(16 * 1024)

    try {
        bmp = BitmapFactory.decodeFile(filePath, options)
    } catch (exception: OutOfMemoryError) {
        exception.printStackTrace()
    }

    try {
        scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
    } catch (exception: OutOfMemoryError) {
        exception.printStackTrace()
    }

    val ratioX = actualWidth / options.outWidth.toFloat()
    val ratioY = actualHeight / options.outHeight.toFloat()
    val middleX = actualWidth / 2.0f
    val middleY = actualHeight / 2.0f

    val scaleMatrix = Matrix()
    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

    val canvas = Canvas(scaledBitmap!!)
    canvas.matrix = scaleMatrix
    canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG))

    val exif: ExifInterface
    try {
        exif = ExifInterface(filePath)

        val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0)
        Log.d("EXIF", "Exif: $orientation")
        val matrix = Matrix()
        when (orientation) {
            6 -> {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            }
            3 -> {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            }
            8 -> {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
        }
        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                scaledBitmap.width, scaledBitmap.height, matrix,
                true)
    } catch (e: IOException) {
        e.printStackTrace()
    }

    val out: FileOutputStream?
    val filename = getFilename()
    try {
        out = FileOutputStream(filename)
        scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }

    return filename
}

private fun getFilename(): String {
    val file = File(Environment.getExternalStorageDirectory().path + "/Pictures/DroidCompressor")
    if (!file.exists()) {
        file.mkdirs()
    }
    return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"
}

private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int,
                                  reqHeight: Int): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
        val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
        inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
    }
    val totalPixels = (width * height).toFloat()
    val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
    while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
        inSampleSize++
    }
    return inSampleSize
}

private fun getRealPathFromURI(context: Context, contentURI: String): String {
    val contentUri = Uri.parse(contentURI)
    val cursor = context.contentResolver.query(contentUri, null, null, null, null)
            ?: return contentUri.path
    cursor.moveToFirst()
    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
    return cursor.getString(index)
}
