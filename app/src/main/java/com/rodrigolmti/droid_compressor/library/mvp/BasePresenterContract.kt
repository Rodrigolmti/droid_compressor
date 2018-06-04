package com.rodrigolmti.droid_compressor.library.mvp

interface BasePresenterContract<in V : BaseView> {

    fun attachView(view: V)

    fun isAttached(): Boolean

    fun detachView()
}