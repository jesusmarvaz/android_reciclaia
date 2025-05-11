package com.ingencode.reciclaia.ui.screens.app.history

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.domain.model.ClassificationModel
import com.ingencode.reciclaia.ui.screens.imagevisor.ImageVisorActivity
import com.ingencode.reciclaia.utils.setTint
import com.ingencode.reciclaia.utils.toFormattedStringDate
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-10.
 */
class ClassificationListAdapter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val processingInfoListener: IProcessingInfoListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_SPACE = 0
    private val TYPE_ITEM = 1
    private val list: ArrayList<ClassificationModel?> = arrayListOf()
    override fun getItemCount(): Int = list.size
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_SPACE else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SPACE -> {
                val spaceView = View(parent.context)
                val heightInDp = 60
                val density = parent.resources.displayMetrics.density
                val heightInPx = (heightInDp * density).toInt()
                spaceView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    heightInPx
                )
                SpaceViewHolder(spaceView)
            }

            TYPE_ITEM -> {
                ClassificationViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_classification, parent, false)
                )
            }

            else -> throw IllegalArgumentException("Debe coincidir el tipo de la lista")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_ITEM) (holder as ClassificationViewHolder).bind(
            list[position],
            context, processingInfoListener
        )
    }

    fun addAll(list: List<ClassificationModel>) {
        this.list.clear()
        this.list.add(null)
        this.list.addAll(list)
        //notifyDataSetChanged()
        this.list.forEachIndexed { i,item -> notifyItemInserted(i) }
    }

    class ClassificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemRoot: ConstraintLayout = itemView.findViewById<ConstraintLayout>(R.id.item_root)
        val timestamp: TextView = itemView.findViewById(R.id.tv_date)
        val imageProcessing: ImageView = itemView.findViewById(R.id.iv_processing)
        val imageCapture: ImageView = itemView.findViewById(R.id.iv_pic)
        val tagMapped: TextView = itemView.findViewById(R.id.tv_mapped_tag)
        val processingMapped: TextView = itemView.findViewById(R.id.tv_mapped_processing)

        fun bind(item: ClassificationModel?, c: Context, listener: IProcessingInfoListener) {
            itemView.setOnClickListener {
                val intent = Intent(c, ImageVisorActivity::class.java)
                    .apply { putExtra("id", item?.getShaID()) }
                c.startActivity(intent)
            }

            /*TODO imageProcessing.setOnClickListener {
                val intent = Intent(c, Processing::class.java)
                    .apply { putExtra("id", item?.getShaID()) }
                c.startActivity(intent)}*/
            val data = item?.classificationData
            data?.let { data ->
                data.backgroundColor()?.let {
                    val color = ContextCompat.getColor(c, it)
                    //deprecated -> itemRoot.background.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    itemRoot.background.setTint(color)
                }
                data.textColor()?.let {
                    val color = ContextCompat.getColor(c, it)
                    tagMapped.setTextColor(color)
                    processingMapped.setTextColor(color)
                    timestamp.setTextColor(color)
                }
            }
            val category = item?.findCategories()?.first()
            val processing = category?.processing?.first()

            imageProcessing.setOnClickListener { listener.onInfoClicked(processing!!) }

            processing?.idColor?.let {
                //imageProcessing.setImageDrawable(ContextCompat.getDrawable(itemView.context, it))
                imageProcessing.setTint(processing.idColor)
                imageProcessing.setOnClickListener { listener.onInfoClicked(processing) }
            }

            tagMapped.isSelected = true
            category?.tags?.first()?.idStringName?.let {
                val percent = ((data?.topPrediction?.confidence ?: 0f) * 100).toInt()
                val tagAndAccuracyText = "${category.tags.first().tag} | %d%%".format(percent)
                val name = c.getString(it)
                tagMapped.text = "%s - %s".format(name, tagAndAccuracyText)
            }
            processing?.idStringTitle?.let { processingMapped.text = c.getString(it) }
            timestamp.text = data?.timestamp?.toFormattedStringDate()
            Glide.with(c).load(item?.uri).into(imageCapture)
        }
    }

    class SpaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}