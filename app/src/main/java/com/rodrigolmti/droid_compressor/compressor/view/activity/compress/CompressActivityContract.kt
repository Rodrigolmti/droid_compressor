package com.rodrigolmti.droid_compressor.compressor.view.activity.compress

import android.content.Context
import com.rodrigolmti.droid_compressor.library.entity.Image
import com.rodrigolmti.droid_compressor.library.mvp.BasePresenterContract
import com.rodrigolmti.droid_compressor.library.mvp.BaseView

object CompressActivityContract {

    interface View : BaseView {

        fun setLoadingVisibility(visible: Boolean)

        fun onCompressingCompleted(images: List<Image>)

        fun onError()

    }

    interface Presenter : BasePresenterContract<View> {

        fun startCompressing(context: Context)

    }
}