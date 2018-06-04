package com.rodrigolmti.droid_compressor.library.mvp

import android.os.Handler
import android.os.Looper

open class BasePresenter<V : BaseView> : BasePresenterContract<V> {

    private val main = Handler(Looper.getMainLooper())
    protected var mView: V? = null

    override fun attachView(view: V) {
        mView = view
    }

    override fun isAttached(): Boolean {
        return mView != null
    }

    override fun detachView() {
        mView = null
    }

    protected fun runOnUiIfAvailable(runnable: Runnable) {
        main.post({
            if (isAttached()) {
                runnable.run()
            }
        })
    }
}