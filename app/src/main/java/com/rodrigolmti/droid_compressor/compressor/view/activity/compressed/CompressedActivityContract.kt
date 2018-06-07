package com.rodrigolmti.droid_compressor.compressor.view.activity.compressed

import android.content.Context
import com.rodrigolmti.droid_compressor.compressor.model.entity.Image
import com.rodrigolmti.droid_compressor.library.mvp.BasePresenterContract
import com.rodrigolmti.droid_compressor.library.mvp.BaseView

object CompressedActivityContract {

    interface View : BaseView {

        fun setLoadingVisibility(visible: Boolean)

        fun onCompressedImagesLoad(images: List<Image>)

        fun onDeletedImages()

        fun onError()

    }

    interface Presenter : BasePresenterContract<View> {

        fun loadCompressedImages(context: Context)

        fun deleteImages(images: List<Image>)

    }
}