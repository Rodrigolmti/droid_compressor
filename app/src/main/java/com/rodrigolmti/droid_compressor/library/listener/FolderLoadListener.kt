package com.rodrigolmti.droid_compressor.library.listener

import com.rodrigolmti.droid_compressor.compressor.model.entity.Folder

interface FolderLoaderListener {

    fun onFolderLoaded(folders: List<Folder>)

}