package com.rodrigolmti.droid_compressor.compressor.view.activity.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import com.rodrigolmti.droid_compressor.R
import com.rodrigolmti.droid_compressor.compressor.manager.SelectedImagesManager
import com.rodrigolmti.droid_compressor.compressor.view.activity.compress.CompressActivity
import com.rodrigolmti.droid_compressor.compressor.view.activity.compressed.CompressedActivity
import com.rodrigolmti.droid_compressor.compressor.view.activity.images.ImagesActivity
import com.rodrigolmti.droid_compressor.compressor.view.adapter.FolderAdapter
import com.rodrigolmti.droid_compressor.library.base.activity.BaseActivity
import com.rodrigolmti.droid_compressor.library.entity.Folder
import com.rodrigolmti.droid_compressor.library.extensions.gone
import com.rodrigolmti.droid_compressor.library.extensions.visible
import com.rodrigolmti.droid_compressor.library.utils.DialogHelper
import com.rodrigolmti.droid_compressor.library.utils.PermissionHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_empty.*
import kotlinx.android.synthetic.main.layout_loading.*

class MainActivity : BaseActivity<MainActivityContract.View, MainActivityContract.Presenter>(), FolderAdapter.OnItemClickListener, MainActivityContract.View {

    override var mPresenter: MainActivityContract.Presenter = MainActivityPresenter()
    private lateinit var folderAdapter: FolderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkStoragePermission()
    }

    override fun onResume() {
        super.onResume()
        updateImagesCount()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (permissions[0] == android.Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                setup()
            }
        }
    }

    override fun onFolderListLoad(folders: List<Folder>) {
        if (folders.isNotEmpty()) {
            folderAdapter = FolderAdapter(this, folders, this)
            recyclerView.adapter = folderAdapter
            recyclerView.visible()
            emptyView.gone()
            return
        }
        emptyView.visible()
        recyclerView.gone()
        menuCompress.gone()
    }

    override fun setLoadingVisibility(visible: Boolean) {
        if (visible) {
            progressBar.visible()
            content.gone()
        } else {
            progressBar.gone()
            content.visible()
        }
    }

    override fun onError() {
        DialogHelper.displayMessage(this, getString(R.string.something_wrong_error), null)
    }

    override fun itemOnClick(item: Folder) {
        searchImagesByFolder(item)
    }

    override fun searchImagesByFolder(folder: Folder) {
        val intent = Intent(this, ImagesActivity::class.java)
        intent.putExtra("action.name.item", folder)
        startActivity(intent)
    }

    private fun checkStoragePermission() {
        PermissionHelper.checkPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                object : PermissionHelper.PermissionAskListener {
                    override fun onPermissionAsk() {
                        ActivityCompat.requestPermissions(this@MainActivity,
                                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
                    }

                    override fun onPermissionPreviouslyDenied() {

                    }

                    override fun onPermissionDisabled() {

                    }

                    override fun onPermissionGranted() {
                        setup()
                    }
                })
    }

    private fun setup() {
        mPresenter.loadFolderList(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()
        menuHistoric.setOnClickListener({
            startActivity(Intent(this, CompressedActivity::class.java))
        })
        menuCompress.setOnClickListener({
            DialogHelper.showConfirmation(this@MainActivity,
                    getString(R.string.activity_main_compress_confirmation),
                    getString(android.R.string.yes), Runnable {
                startActivity(Intent(this, CompressActivity::class.java))
            }, getString(android.R.string.no), null)
        })
    }

    private fun updateImagesCount() {
        val count = SelectedImagesManager.getImagesTotalSizeFormatted()
        if (count.isNotEmpty()) {
            textViewCount.text = SelectedImagesManager.getImagesCount().toString()
            textViewImagesSize.text = getString(R.string.activity_main_images_size, count)
            textViewImagesSize.visible()
            menuCompress.visible()
        } else {
            textViewImagesSize.gone()
            textViewCount.text = "0"
            menuCompress.gone()
        }
    }
}
