package com.rodrigolmti.droid_compressor.compressor.view.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.rodrigolmti.droid_compressor.R
import com.rodrigolmti.droid_compressor.compressor.manager.SelectedImagesManager
import com.rodrigolmti.droid_compressor.library.entity.Image
import com.rodrigolmti.droid_compressor.library.extensions.gone
import com.rodrigolmti.droid_compressor.library.extensions.visible
import kotlinx.android.synthetic.main.row_image.view.*

class ImageAdapter(private val context: Context, private val list: List<Image>, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val filteredList: ArrayList<Image> = ArrayList()

    init {
        filteredList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Item(LayoutInflater.from(context).inflate(R.layout.row_image, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Item).bindData(context, list[position], listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(context: Context, item: Image, listener: OnItemClickListener) {
            val selected = SelectedImagesManager.containsImage(item)
            itemView.setOnClickListener { listener.itemOnClick(item) }
            if (selected) {
                itemView.imageViewCheck.visible()
                itemView.viewAlpha.alpha = 0.5F
            } else {
                itemView.imageViewCheck.gone()
                itemView.viewAlpha.alpha = 0F
            }
            Glide.with(context).load(item.path).thumbnail(0.5f).into(itemView.imageView)
        }
    }

    interface OnItemClickListener {
        fun itemOnClick(item: Image)
    }
}