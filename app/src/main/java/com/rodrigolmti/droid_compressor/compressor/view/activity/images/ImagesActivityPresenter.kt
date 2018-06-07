package com.rodrigolmti.droid_compressor.compressor.view.activity.images

import android.content.Context
import com.rodrigolmti.droid_compressor.compressor.model.entity.Folder
import com.rodrigolmti.droid_compressor.compressor.model.entity.Image
import com.rodrigolmti.droid_compressor.library.listener.ImageLoaderListener
import com.rodrigolmti.droid_compressor.library.mvp.BasePresenter
import com.rodrigolmti.droid_compressor.library.utils.runnables.FileLoader

class ImagesActivityPresenter : BasePresenter<ImagesActivityContract.View>(), ImagesActivityContract.Presenter {

    override fun loadImageList(folder: Folder, context: Context) {
        runOnUiIfAvailable(Runnable { mView?.setLoadingVisibility(true) })
        FileLoader(context).loadDeviceImages(folder, object : ImageLoaderListener {
            override fun onImageLoaded(folders: List<Image>) {
                runOnUiIfAvailable(Runnable {
                    mView?.setLoadingVisibility(false)
                    mView?.onImageListLoad(folders)
                })
            }
        })
    }
}