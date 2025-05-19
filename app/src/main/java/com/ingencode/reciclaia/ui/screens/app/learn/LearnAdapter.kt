package com.ingencode.reciclaia.ui.screens.app.learn

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ingencode.reciclaia.domain.model.LearnModelBundle
import javax.inject.Inject
import com.ingencode.reciclaia.R
import androidx.core.net.toUri

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-19.
 */
class LearnAdapter(private val learnElements: ArrayList<LearnModelBundle>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_TITLE = 0
        private const val VIEW_TYPE_WASTE = 1
        private const val VIEW_TYPE_PROCESSING = 2
        private const val VIEW_TYPE_URL = 3
    }

    override fun getItemViewType(position: Int): Int {
        return when (learnElements[position]) {
            is LearnModelBundle.TitleItem -> VIEW_TYPE_TITLE
            is LearnModelBundle.WasteItem -> VIEW_TYPE_WASTE
            is LearnModelBundle.ProcessingItem -> VIEW_TYPE_PROCESSING
            is LearnModelBundle.UrlItem -> VIEW_TYPE_URL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TITLE -> TitleViewHolder(getViewInflated(parent, R.layout.item_title_holder))
            VIEW_TYPE_WASTE -> WasteTypeHolder(getViewInflated(parent, R.layout.item_wastetype_holder))
            VIEW_TYPE_PROCESSING -> ProcessingTypeHolder(getViewInflated(parent, R.layout.item_processingtype_holder))
            VIEW_TYPE_URL -> UrlHolder(getViewInflated(parent, R.layout.item_url_holder))
            else -> throw IllegalArgumentException("Ningún layout para el elemento")
        }
    }

    override fun getItemCount(): Int = learnElements.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = learnElements[position]
        when (currentItem) {
            is LearnModelBundle.TitleItem -> (holder as TitleViewHolder).bind(currentItem)
            is LearnModelBundle.WasteItem -> (holder as WasteTypeHolder).bind(currentItem)
            is LearnModelBundle.ProcessingItem -> (holder as ProcessingTypeHolder).bind(currentItem)
            is LearnModelBundle.UrlItem -> (holder as UrlHolder).bind(currentItem)
        }
    }

    private fun getViewInflated(parent: ViewGroup, @LayoutRes layoutId: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }

    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.tv_title_holder)
        fun bind(item: LearnModelBundle.TitleItem) { titleView.text = item.title }
    }

    class WasteTypeHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title_waste_item)
        val description: TextView = itemView.findViewById(R.id.tv_description_waste_type)

        fun bind(item: LearnModelBundle.WasteItem) {
            title.text = item.wasteTypeModel.wasteType
            description.text = item.wasteTypeModel.description
        }
    }

    class ProcessingTypeHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title_processing_item)
        val description: TextView = itemView.findViewById(R.id.tv_description_processing_type)
        val backdrop: ImageView = itemView.findViewById(R.id.iv_backdrop)
        fun bind(item: LearnModelBundle.ProcessingItem) {
            itemView.setOnClickListener { launchUrl(item.processingInfoModel.processingName.url, itemView.context) }
            Glide.with(itemView.context).load(item.processingInfoModel.imageUri).into(backdrop)
            title.text = item.processingInfoModel.processingName.name
            description.text = item.processingInfoModel.description
        }
    }


    class UrlHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title_url_item)
        val description: TextView = itemView.findViewById(R.id.tv_description_url_type)
        fun bind(item: LearnModelBundle.UrlItem) {
            itemView.setOnClickListener { launchUrl(item.urlType.url, itemView.context) }
            title.text = item.urlType.title
            description.text = item.urlType.description
        }
    }
}

fun launchUrl(urlString: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, urlString.toUri())
    context.startActivity(intent)
}