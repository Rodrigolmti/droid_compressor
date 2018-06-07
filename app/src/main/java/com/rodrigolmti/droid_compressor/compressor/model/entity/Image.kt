package com.rodrigolmti.droid_compressor.compressor.model.entity

import android.database.Cursor
import android.provider.MediaStore
import java.util.*

class Image constructor(val id: Long, val name: String, val path: String, val date: Date?) {

    companion object {

        fun fromCursor(cursor: Cursor): Image {
            val columnId = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val columnTitle = cursor.getColumnIndex(MediaStore.Images.Media.TITLE)
            val columnData = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            return Image(
                    cursor.getLong(columnId),
                    cursor.getString(columnTitle),
                    cursor.getString(columnData), null)
        }
    }
}