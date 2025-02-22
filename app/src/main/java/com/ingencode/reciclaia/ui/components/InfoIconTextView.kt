package com.ingencode.reciclaia.ui.components

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.utils.setSizeInDp

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-20.
 */
class InfoIconTextView : ConstraintLayout {
    constructor(context: Context): super(context) { initialize(null) }
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs){ initialize(attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle){ initialize(attrs) }

    private lateinit var label: TextView
    private lateinit var detail: TextView
    private lateinit var icon: ImageView
    private lateinit var separator: View

    private fun initialize(attrs: AttributeSet?) {
        val li = LayoutInflater.from(context)
        li.inflate(R.layout.info_icon_textview, this, true)
        label = findViewById(R.id.tv_info_icon)
        detail = findViewById(R.id.tv_info_icon_detail)
        icon = findViewById(R.id.iv_info_icon)
        separator = findViewById(R.id.view_separator)

        if (attrs == null) return

        var ta: TypedArray? = null

        try {
            ta = context.obtainStyledAttributes(attrs, R.styleable.InfoIconTextView)
            val icon: Drawable? = ta.getDrawable(R.styleable.InfoIconTextView_iconSrcId)
            val iconSize = ta.getFloat(R.styleable.InfoIconTextView_iconSizeInDp, 32f)
            val labelText = ta.getString(R.styleable.InfoIconTextView_label)
            val detailText = ta.getString(R.styleable.InfoIconTextView_itemDetail)
            val isSeparatorVisible = ta.getBoolean(R.styleable.InfoIconTextView_isSeparatorVisible, true)

            setIcon(icon)
            setIconSize(iconSize)
            setSeparatorVisible(isSeparatorVisible)
            labelText?.let {  setLabelText(labelText) }
            detailText?.let { setDetail(it) }

        } catch (e: Exception) {
            throw e
        } finally {
            ta?.recycle()
        }
    }

    fun setLabelText(textLabel: String) {
        this.label.text = textLabel
    }

    fun setDetail(text: String) {
        this.detail.text = text
    }

    fun setIcon(id: Drawable?) {
        if(id != null) {
            //icon.background = context.getDrawable(id)
            //icon.background = AppCompatResources.getDrawable(context, id)
            icon.background = id
            icon.visibility = View.VISIBLE
        }
        else icon.visibility = View.GONE
    }

    fun setIconSize(size: Float) {
        icon.setSizeInDp(size)
    }

    fun setSeparatorVisible(visible: Boolean) {
        this.separator.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }
}