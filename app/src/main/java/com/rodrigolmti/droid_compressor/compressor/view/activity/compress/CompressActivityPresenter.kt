package com.rodrigolmti.droid_compressor.compressor.view.activity.compress

import android.content.Context
import com.rodrigolmti.droid_compressor.compressor.manager.SelectedImagesManager
import com.rodrigolmti.droid_compressor.library.entity.Image
import com.rodrigolmti.droid_compressor.library.listener.CompressLoadListener
import com.rodrigolmti.droid_compressor.library.mvp.BasePresenter
import com.rodrigolmti.droid_compressor.library.utils.runnables.CompressImages

class CompressActivityPresenter : BasePresenter<CompressActivityContract.View>(), CompressActivityContract.Presenter {

    override fun startCompressing(context: Context) {
        runOnUiIfAvailable(Runnable { mView?.setLoadingVisibility(true) })
        CompressImages(context).compressSelectedImages(object : CompressLoadListener {

            override fun onImageCompressed(images: List<Image>) {
                runOnUiIfAvailable(Runnable {
                    mView?.setLoadingVisibility(true)
                    SelectedImagesManager.clearSelectedImages()
                    mView?.onCompressingCompleted(images)
                })
            }

            override fun onError() {
                runOnUiIfAvailable(Runnable {
                    mView?.setLoadingVisibility(false)
                    mView?.onError()
                })
            }
        })
    }
}