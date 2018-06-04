package com.rodrigolmti.droid_compressor.compressor.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.rodrigolmti.droid_compressor.R
import com.rodrigolmti.droid_compressor.library.entity.Image
import com.rodrigolmti.droid_compressor.library.extensions.gone
import com.rodrigolmti.droid_compressor.library.extensions.visible
import com.rodrigolmti.droid_compressor.library.utils.DateHelper
import kotlinx.android.synthetic.main.row_image_compressed.view.*
import java.util.ArrayList

class ImageCompressedAdapter(private val context: Context, private val list: List<Image>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val controlSelectedImages = BooleanArray(list.size, { false })
    val selectedImages: List<Image>
        get() {
            val selectedImages = ArrayList<Image>()
            try {
                for (i in controlSelectedImages.indices) {
                    if (controlSelectedImages[i]) {
                        selectedImages.add(list[i])
                    }
                }
            } catch (error: Exception) {
                error.printStackTrace()
            }
            return selectedImages
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Item(LayoutInflater.from(context).inflate(R.layout.row_image_compressed, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Item).bindData(context, list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(context: Context, item: Image, position: Int) {
            itemView.setOnClickListener {
                controlSelectedImages[position] = !controlSelectedImages[position]
                notifyDataSetChanged()
            }
            if (controlSelectedImages[position]) {
                itemView.imageViewCheck.visible()
                itemView.content.alpha = 0.5F
            } else {
                itemView.imageViewCheck.gone()
                itemView.content.alpha = 1.0F
            }
            itemView.textViewName.text = item.name
            itemView.textViewDate.text = DateHelper.dateToString(item.date, DateHelper.full_date)
            Glide.with(context).load(item.path).thumbnail(0.5f).into(itemView.imageView)
        }
    }
}