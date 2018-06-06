package com.rodrigolmti.droid_compressor.compressor.view.activity.compressed

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.rodrigolmti.droid_compressor.R
import com.rodrigolmti.droid_compressor.compressor.view.adapter.ImageCompressedAdapter
import com.rodrigolmti.droid_compressor.library.base.activity.BaseActivity
import com.rodrigolmti.droid_compressor.library.entity.Image
import com.rodrigolmti.droid_compressor.library.extensions.gone
import com.rodrigolmti.droid_compressor.library.extensions.visible
import com.rodrigolmti.droid_compressor.library.utils.DialogHelper
import kotlinx.android.synthetic.main.activity_compressed.*
import kotlinx.android.synthetic.main.layout_empty.*
import kotlinx.android.synthetic.main.layout_loading.*

class CompressedActivity : BaseActivity<CompressedActivityContract.View, CompressedActivityContract.Presenter>(), CompressedActivityContract.View {

    override var mPresenter: CompressedActivityContract.Presenter = CompressedActivityPresenter()
    private lateinit var adapter: ImageCompressedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compressed)
        setup()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.delete_images_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_delete -> {
                if (adapter.selectedImages.isEmpty()) {
                    DialogHelper.displayMessage(this, getString(R.string.activity_compressed_delete_error), null)
                } else {
                    DialogHelper.showConfirmation(this@CompressedActivity,
                            getString(R.string.activity_compressed_delete_message),
                            getString(android.R.string.yes), Runnable {
                        mPresenter.deleteImages(adapter.selectedImages)
                    }, getString(android.R.string.no), null)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun setLoadingVisibility(visible: Boolean) {
        if (visible) {
            progressBar.visible()
            recyclerView.gone()
        } else {
            recyclerView.visible()
            progressBar.gone()
        }
    }

    override fun onCompressedImagesLoad(images: List<Image>) {
        if (images.isNotEmpty()) {
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = ImageCompressedAdapter(this, images)
            recyclerView.adapter = adapter
            recyclerView.hasFixedSize()
            recyclerView.visible()
            emptyView.gone()
        } else {
            recyclerView.gone()
            emptyView.visible()
        }
    }

    override fun onDeletedImages() {
        DialogHelper.displayMessage(this, getString(R.string.activity_compressed_delete_message_success), null)
        mPresenter.loadCompressedImages(this)
    }

    override fun onError() {
        DialogHelper.displayMessage(this, getString(R.string.something_wrong_error), null)
        recyclerView.gone()
        emptyView.visible()
    }

    private fun setup() {
        title = getString(R.string.activity_compressed_title)
        mPresenter.loadCompressedImages(this)
        enableBackButton()
    }
}
