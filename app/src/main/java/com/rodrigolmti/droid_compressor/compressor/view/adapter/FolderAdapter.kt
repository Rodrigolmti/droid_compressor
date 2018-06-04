package com.rodrigolmti.droid_compressor.compressor.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rodrigolmti.droid_compressor.R
import com.rodrigolmti.droid_compressor.library.entity.Folder
import kotlinx.android.synthetic.main.row_folder.view.*

open class FolderAdapter(private val context: Context, private val list: List<Folder>, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val filteredList: ArrayList<Folder> = ArrayList()

    init {
        filteredList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Item(LayoutInflater.from(context).inflate(R.layout.row_folder, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Item).bindData(list[position], listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(item: Folder, listener: OnItemClickListener) {
            itemView.setOnClickListener { listener.itemOnClick(item) }
            itemView.textViewName.text = item.folderName
        }
    }

    interface OnItemClickListener {
        fun itemOnClick(item: Folder)
    }
}