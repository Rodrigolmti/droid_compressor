package com.rodrigolmti.droid_compressor.library.entity

import android.database.Cursor
import android.provider.MediaStore
import java.io.Serializable

class Folder constructor(val folderName: String, val bucketId: String) : Serializable {

    companion object {

        fun fromCursor(cursor: Cursor): Folder {
            val bucketColumnName = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val bucketColumnId = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)
            return Folder(cursor.getString(bucketColumnName), cursor.getString(bucketColumnId))
        }
    }
}