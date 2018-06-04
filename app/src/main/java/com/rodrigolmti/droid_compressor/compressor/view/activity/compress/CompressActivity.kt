package com.rodrigolmti.droid_compressor.compressor.view.activity.compress

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.rodrigolmti.droid_compressor.R
import com.rodrigolmti.droid_compressor.compressor.manager.SelectedImagesManager
import com.rodrigolmti.droid_compressor.compressor.view.adapter.ImageCompressedAdapter
import com.rodrigolmti.droid_compressor.library.base.activity.BaseActivity
import com.rodrigolmti.droid_compressor.library.entity.Image
import com.rodrigolmti.droid_compressor.library.extensions.gone
import com.rodrigolmti.droid_compressor.library.extensions.visible
import com.rodrigolmti.droid_compressor.library.utils.DialogHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_loading.*
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity


class CompressActivity : BaseActivity<CompressActivityContract.View, CompressActivityContract.Presenter>(), CompressActivityContract.View {

    override var mPresenter: CompressActivityContract.Presenter = CompressActivityPresenter()
    private lateinit var adapter: ImageCompressedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compress)
        setup()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.compressed_images_menu, menu)
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
                    DialogHelper.displayMessage(this, getString(R.string.activity_compress_minimum_images), null)
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
        val uris: ArrayList<Uri> = ArrayList()
        for (image in images) {
            uris.add(Uri.parse(image.path))
        }
        val intent = Intent()
        intent.action = Intent.ACTION_SEND_MULTIPLE
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.")
        intent.type = "image/jpeg"
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        startActivity(Intent.createChooser(intent, ""))
    }
}
