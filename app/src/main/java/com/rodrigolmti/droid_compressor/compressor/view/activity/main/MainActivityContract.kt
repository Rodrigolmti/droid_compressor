package com.rodrigolmti.droid_compressor.compressor.view.activity.main

import android.content.Context
import com.rodrigolmti.droid_compressor.library.entity.Folder
import com.rodrigolmti.droid_compressor.library.mvp.BasePresenterContract
import com.rodrigolmti.droid_compressor.library.mvp.BaseView

object MainActivityContract {

    interface View : BaseView {

        fun onFolderListLoad(folders: List<Folder>)

        fun setLoadingVisibility(visible: Boolean)

        fun searchImagesByFolder(folder: Folder)

        fun onError()

    }

    interface Presenter : BasePresenterContract<View> {

        fun loadFolderList(context: Context)

    }

}