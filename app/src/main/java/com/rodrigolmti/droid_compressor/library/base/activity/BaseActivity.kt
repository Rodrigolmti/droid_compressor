package com.rodrigolmti.droid_compressor.library.base.activity

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rodrigolmti.droid_compressor.library.mvp.BasePresenterContract
import com.rodrigolmti.droid_compressor.library.mvp.BaseView

abstract class BaseActivity<in V : BaseView, T : BasePresenterContract<V>> : AppCompatActivity(), BaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        mPresenter.attachView(this as V)
    }

    protected fun enableBackButton() {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    fun getContext(): Context = this

    protected abstract var mPresenter: T

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}