package com.rodrigolmti.droid_compressor.compressor.view.activity.compress

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.rodrigolmti.droid_compressor.R
import com.rodrigolmti.droid_compressor.compressor.manager.SelectedImagesManager
import com.rodrigolmti.droid_compressor.compressor.view.adapter.ImageCompressedAdapter
import com.rodrigolmti.droid_compressor.library.base.activity.BaseActivity
import com.rodrigolmti.droid_compressor.compressor.model.entity.Image
import com.rodrigolmti.droid_compressor.library.utils.extensions.gone
import com.rodrigolmti.droid_compressor.library.utils.extensions.visible
import com.rodrigolmti.droid_compressor.library.utils.functions.displayMessage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_loading.*

class CompressActivity : BaseActivity<CompressActivityContract.View, CompressActivityContract.Presenter>(), CompressActivityContract.View {

    override var mPresenter: CompressActivityContract.Presenter = CompressActivityPresenter()
    private lateinit var adapter: ImageCompressedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compress)
        setup()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share_images_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_share -> {
                if (adapter.selectedImages.isEmpty()) {
                    displayMessage(this, getString(R.string.activity_compress_minimum_images), null)
                } else {
                    shareMultiple(adapter.selectedImages)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun onCompressingCompleted(images: List<Image>) {
        adapter = ImageCompressedAdapter(this, images)
        recyclerView.adapter = adapter
    }

    override fun setLoadingVisibility(visible: Boolean) {
        if (visible) {
            progressBar.visible()
        } else {
            progressBar.gone()
        }
    }

    override fun onError() {
        displayMessage(this, getString(R.string.something_wrong_error), null)
    }

    private fun setup() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        mPresenter.startCompressing(this)
        recyclerView.hasFixedSize()
        setupActionBar()
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = getString(R.string.activity_compress_title)
            actionBar.subtitle = resources.getQuantityString(R.plurals.image_number, SelectedImagesManager.getImagesCount())
            enableBackButton()
        }
    }

    private fun shareMultiple(images: List<Image>) {
        try {

            val uris: ArrayList<Uri> = ArrayList()
            for (image in images) {
                uris.add(Uri.parse(image.path))
            }
            val shareIntent = Intent()
            if (uris.size == 1) {
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uris.first().path))
            } else {
                shareIntent.action = Intent.ACTION_SEND_MULTIPLE
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            }
            shareIntent.type = "image/jpeg"
            startActivity(Intent.createChooser(shareIntent, "Share Deal"))

        } catch (error: Exception) {
            error.stackTrace
        }
    }
}
