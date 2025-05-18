package com.ingencode.reciclaia.ui.screens.app.home.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.domain.model.HomeClassificationModel
import com.ingencode.reciclaia.ui.screens.app.home.IImageSelector
import com.ingencode.reciclaia.ui.screens.imagevisor.ImageVisorActivity
import com.ingencode.reciclaia.utils.sha256
import javax.inject.Inject

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-15.
 */
class ClassificationsHomeAdapter @Inject constructor(private val context: Context, val imageSelector: IImageSelector) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_ADD = 0
    private val TYPE_ITEM = 1
    private val list: ArrayList<HomeClassificationModel?> = arrayListOf()
    fun addAll(list: List<HomeClassificationModel> = arrayListOf()) {
        this.list.clear()
        this.list.add(null)
        this.list.addAll(list)
        this.list.forEachIndexed { i, item -> notifyItemInserted(i) }
    }

    override fun getItemCount(): Int = list.size
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_ADD else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ADD -> {
                val viewAdd = AddItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_classification_home_add, parent, false), context, imageSelector)
                viewAdd
            }
            TYPE_ITEM -> {
                val viewItem = ModelItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_classification_home, parent, false), context)
                return viewItem
            }
            else -> throw IllegalArgumentException("Debe coincidir el tipo de la lista")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder.itemViewType == TYPE_ADD) (holder as AddItemViewHolder).bind() else (holder as ModelItemViewHolder).bind(item!!)
    }


    class AddItemViewHolder(itemView: View, val context: Context, val imageSelector: IImageSelector): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.setOnClickListener {
                imageSelector.launchImageSelector()
            }
        }
    }
    class ModelItemViewHolder(itemView: View, val context: Context): RecyclerView.ViewHolder(itemView) {
        val tag: TextView = itemView.findViewById(R.id.tv_tag)
        val image: ImageView = itemView.findViewById(R.id.iv_capture)
        val confidence: TextView = itemView.findViewById(R.id.tv_tag_confidence)

        fun bind(item: HomeClassificationModel) {
            itemView.setOnClickListener {
                val intent = Intent(context, ImageVisorActivity::class.java).apply { putExtra("id", item.uri.toString().sha256()) }
                context.startActivity(intent)
            }
            tag.text = item.tag
            tag.isSelected = true
            confidence.text = "%d%%".format((item.confidence * 100).toInt())
            Glide.with(context).load(item.uri).into(image)
        }
    }
}