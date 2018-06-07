package com.rodrigolmti.droid_compressor.library.listener

interface PermissionAskListener {

    fun onPermissionAsk()

    fun onPermissionPreviouslyDenied()

    fun onPermissionDisabled()

    fun onPermissionGranted()
}