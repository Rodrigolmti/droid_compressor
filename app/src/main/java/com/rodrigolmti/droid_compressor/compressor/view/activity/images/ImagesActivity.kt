package com.rodrigolmti.droid_compressor.compressor.view.activity.images

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.rodrigolmti.droid_compressor.R
import com.rodrigolmti.droid_compressor.compressor.manager.SelectedImagesManager
import com.rodrigolmti.droid_compressor.compressor.view.component.GridSpacingItemDecoration
import com.rodrigolmti.droid_compressor.compressor.view.adapter.ImageAdapter
import com.rodrigolmti.droid_compressor.library.base.activity.BaseActivity
import com.rodrigolmti.droid_compressor.library.entity.Folder
import com.rodrigolmti.droid_compressor.library.entity.Image
import com.rodrigolmti.droid_compressor.library.extensions.gone
import com.rodrigolmti.droid_compressor.library.extensions.visible
import com.rodrigolmti.droid_compressor.library.utils.DialogHelper
import kotlinx.android.synthetic.main.activity_images.*
import kotlinx.android.synthetic.main.layout_empty.*
import kotlinx.android.synthetic.main.layout_loading.*

class ImagesActivity : BaseActivity<ImagesActivityContract.View, ImagesActivityContract.Presenter>(), ImageAdapter.OnItemClickListener, ImagesActivityContract.View {

    override var mPresenter: ImagesActivityContract.Presenter = ImagesActivityPresenter()
    private lateinit var imagesAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)
        setup()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onImageListLoad(images: List<Image>) {
        if (images.isNotEmpty()) {
            imagesAdapter = ImageAdapter(this, images, this)
            recyclerView.adapter = imagesAdapter
            recyclerView.visible()
            emptyView.gone()
            return
        }
        emptyView.visible()
        recyclerView.gone()
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

    override fun onError() {
        DialogHelper.displayMessage(this, getString(R.string.something_wrong_error), null)
    }

    override fun itemOnClick(item: Image) {
        SelectedImagesManager.handleImage(item)
        imagesAdapter.notifyDataSetChanged()
    }

    private fun setup() {
        if (intent.hasExtra("action.name.item")) {
            val folder: Folder = intent.getSerializableExtra("action.name.item") as Folder
            mPresenter.loadImageList(folder, this)
            val gridLayoutManager = GridLayoutManager(this, 4)
            gridLayoutManager.spanCount = 4
            recyclerView.layoutManager = gridLayoutManager
            val itemOffsetDecoration = GridSpacingItemDecoration(
                    4,
                    resources.getDimensionPixelSize(R.dimen.item_padding),
                    false
            )
            recyclerView.addItemDecoration(itemOffsetDecoration)
            setupActionBar(folder)
        }
    }

    private fun setupActionBar(folder: Folder) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = folder.folderName
            actionBar.subtitle = getString(R.string.activity_images_subtitle)
            enableBackButton()
        }
    }
}
