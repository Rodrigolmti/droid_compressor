package com.rodrigolmti.droid_compressor.compressor.view.activity.main

import android.content.Context
import com.rodrigolmti.droid_compressor.library.entity.Folder
import com.rodrigolmti.droid_compressor.library.listener.FolderLoaderListener
import com.rodrigolmti.droid_compressor.library.mvp.BasePresenter
import com.rodrigolmti.droid_compressor.library.utils.runnables.FileLoader

class MainActivityPresenter : BasePresenter<MainActivityContract.View>(), MainActivityContract.Presenter {

    override fun loadFolderList(context: Context) {
        runOnUiIfAvailable(Runnable { mView?.setLoadingVisibility(true) })
        FileLoader(context).loadDeviceFolders(object : FolderLoaderListener {
            override fun onFolderLoaded(folders: List<Folder>) {
                runOnUiIfAvailable(Runnable {
                    mView?.setLoadingVisibility(false)
                    mView?.onFolderListLoad(folders)
                })
            }
        })
    }
}