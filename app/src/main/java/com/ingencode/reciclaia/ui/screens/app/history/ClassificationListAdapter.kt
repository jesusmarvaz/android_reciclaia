package com.ingencode.reciclaia.ui.screens.app.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.domain.model.ClassificationModel
import com.ingencode.reciclaia.utils.setTint
import com.ingencode.reciclaia.utils.toFormattedStringDate
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-10.
 */
class ClassificationListAdapter @Inject constructor(@ApplicationContext private val context: Context): RecyclerView.Adapter<ClassificationListAdapter.ClassificationViewHolder>() {
    private val list: ArrayList<ClassificationModel> = arrayListOf()
    override fun getItemCount(): Int = list.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassificationViewHolder
    = ClassificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_classification, parent, false))

    override fun onBindViewHolder(holder: ClassificationViewHolder, position: Int) = holder.bind(list[position])

    fun addAll(list: List<ClassificationModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class ClassificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timestamp: TextView = itemView.findViewById(R.id.tv_date)
        val image: ImageView = itemView.findViewById(R.id.iv_processing)
        //TODO: finish
        fun bind(item: ClassificationModel) {
            timestamp.text = item.classificationData?.timestamp?.toFormattedStringDate()
            val processing = item.findCategories()?.first()?.processing?.first()
            processing?.idDrawableRes?.let {
                image.setImageDrawable(ContextCompat.getDrawable(itemView.context, it))
                image.setTint(processing.idColor)
            }
        }
    }
}