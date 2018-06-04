package com.rodrigolmti.droid_compressor.library.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat

object PermissionHelper {

    private val isMNC: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    private fun shouldAskPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private fun shouldAskPermission(context: Context, permission: String): Boolean {
        if (shouldAskPermission()) {
            val permissionResult = ActivityCompat.checkSelfPermission(context, permission)
            return permissionResult != PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasSelfPermission(context: Context?, permissions: Array<String>?): Boolean {
        if (context == null || permissions == null) return false
        if (!isMNC) {
            return true
        }
        for (permission in permissions) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun checkPermission(context: Activity, permission: String, listener: PermissionAskListener) {

        if (shouldAskPermission(context, permission)) {

            if (context.shouldShowRequestPermissionRationale(permission)) {
                listener.onPermissionPreviouslyDenied()
            } else {

                if (isFirstTimeAskingPermission(context, permission)) {
                    firstTimeAskingPermission(context, permission)
                    listener.onPermissionAsk()
                } else {

                    listener.onPermissionDisabled()
                }
            }
        } else {
            listener.onPermissionGranted()
        }
    }

    interface PermissionAskListener {

        fun onPermissionAsk()

        fun onPermissionPreviouslyDenied()

        fun onPermissionDisabled()

        fun onPermissionGranted()
    }

    private fun firstTimeAskingPermission(context: Context, permission: String) {
        val sharedPreference = context.getSharedPreferences("permissions", MODE_PRIVATE)
        sharedPreference.edit().putBoolean(permission, false).apply()
    }

    private fun isFirstTimeAskingPermission(context: Context, permission: String): Boolean {
        return context.getSharedPreferences("permissions", MODE_PRIVATE).getBoolean(permission, true)
    }
}