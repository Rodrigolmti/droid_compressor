package com.rodrigolmti.droid_compressor.library.utils

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import com.rodrigolmti.droid_compressor.R

object DialogHelper {

    fun showProgressDialog(context: Context, title: String, message: String,
                           cancelListener: DialogInterface.OnCancelListener?,
                           onShowListener: DialogInterface.OnShowListener,
                           onDismissListener: DialogInterface.OnDismissListener): ProgressDialog {
        val progressDialog = ProgressDialog(context, R.style.CustomDialog)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(cancelListener != null)
        progressDialog.setOnCancelListener(cancelListener)
        progressDialog.setOnShowListener(onShowListener)
        progressDialog.setOnDismissListener(onDismissListener)
        progressDialog.setTitle(title)
        progressDialog.setMessage(message)
        progressDialog.setIcon(R.mipmap.ic_launcher)
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
        progressDialog.show()
        return progressDialog
    }

    fun showConfirmation(context: Context, message: String,
                         positiveButtonCaption: String, positiveButtonCallback: Runnable?,
                         negativeButtonCaption: String, negativeButtonCallback: Runnable?) {
        val builder = AlertDialog.Builder(context, R.style.CustomDialog)
        builder.setTitle(context.getString(R.string.app_name))
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setPositiveButton(positiveButtonCaption) { _, _ -> positiveButtonCallback?.run() }
        if (!TextUtils.isEmpty(negativeButtonCaption)) {
            builder.setNegativeButton(negativeButtonCaption) { _, _ ->
                negativeButtonCallback?.run()
            }
        }
        val dialog = builder.create()
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        dialog.show()
        (dialog.findViewById<View>(android.R.id.message) as TextView).movementMethod = LinkMovementMethod.getInstance()
    }

    fun displayMessage(context: Context, message: String,
                       positiveCallback: Runnable?) {
        val alertDialog = AlertDialog.Builder(context,
                R.style.CustomDialog)
        alertDialog.setTitle(context.getString(R.string.app_name))
        alertDialog.setIcon(R.mipmap.ic_launcher)
        alertDialog.setMessage(message)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton(context.resources.getString(android.R.string.ok)
        ) { _, _ -> positiveCallback?.run() }
        val dialog = alertDialog.create()
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        dialog.show()
    }
}