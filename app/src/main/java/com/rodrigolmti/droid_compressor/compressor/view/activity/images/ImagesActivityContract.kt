package com.rodrigolmti.droid_compressor.compressor.view.activity.images

import android.content.Context
import com.rodrigolmti.droid_compressor.compressor.model.entity.Folder
import com.rodrigolmti.droid_compressor.compressor.model.entity.Image
import com.rodrigolmti.droid_compressor.library.mvp.BasePresenterContract
import com.rodrigolmti.droid_compressor.library.mvp.BaseView

object ImagesActivityContract {

    interface View : BaseView {

        fun onImageListLoad(images: List<Image>)

        fun setLoadingVisibility(visible: Boolean)

        fun onError()

    }

    interface Presenter : BasePresenterContract<View> {

        fun loadImageList(folder: Folder, context: Context)

    }
}