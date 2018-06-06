package com.rodrigolmti.droid_compressor.compressor.view.activity.compressed

import android.content.Context
import com.rodrigolmti.droid_compressor.library.entity.Image
import com.rodrigolmti.droid_compressor.library.listener.ImageLoaderListener
import com.rodrigolmti.droid_compressor.library.mvp.BasePresenter
import com.rodrigolmti.droid_compressor.library.utils.runnables.FileLoader
import java.io.File

class CompressedActivityPresenter : BasePresenter<CompressedActivityContract.View>(), CompressedActivityContract.Presenter {

    override fun loadCompressedImages(context: Context) {
        runOnUiIfAvailable(Runnable { mView?.setLoadingVisibility(true) })
        FileLoader(context).loadDeviceImages(null, object : ImageLoaderListener {
            override fun onImageLoaded(images: List<Image>) {
                runOnUiIfAvailable(Runnable {
                    mView?.setLoadingVisibility(false)
                    mView?.onCompressedImagesLoad(images)
                })
            }
        })
    }

    override fun deleteImages(images: List<Image>) {
        runOnUiIfAvailable(Runnable { mView?.setLoadingVisibility(true) })
        for (image in images) {
            val file = File(image.path)
            if (file.isFile && file.exists() && file.delete()) {
                continue
            } else {
                mView?.onError()
                break
            }
        }
        mView?.onDeletedImages()
    }
}