package com.ingencode.reciclaia.ui.components.visor

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.createBitmap
import com.ingencode.reciclaia.R

/**
Created with ❤ by jesusmarvaz on 2025-04-09.
 */

class ComposedVisor: ConstraintLayout {
    enum class RotationMode { Clockwise, Counterclockwise }
    constructor(context: Context): super(context) { initialize(null) }
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs){ initialize(attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle){ initialize(attrs) }

    private lateinit var editableVisor: EditableVisor
    private lateinit var clockWiseButton: ImageButton
    private lateinit var counterClockWiseButton: ImageButton
    var isEditable: Boolean = true
        set(value) {
            editableVisor.isEnabled = value
            field = value
        }

    private fun initialize(attrs: AttributeSet?) {
        val li = LayoutInflater.from(context)
        li.inflate(R.layout.composed_visor_layout, this, true)
        editableVisor = findViewById<EditableVisor>(R.id.editable_visor)

        clockWiseButton = findViewById<ImageButton>(R.id.iv_clockwise)
        clockWiseButton.setOnClickListener { if (isEditable) rotate(RotationMode.Clockwise) }

        counterClockWiseButton = findViewById<ImageButton>(R.id.iv_counterclockwise)
        counterClockWiseButton.setOnClickListener { if (isEditable) rotate(RotationMode.Counterclockwise) }

        if (attrs == null) return

        var ta: TypedArray? = null
        try {
            ta = context.obtainStyledAttributes(attrs, R.styleable.ComposedVisor)
            isEditable = ta.getBoolean(R.styleable.ComposedVisor_isEditable, true)

        } catch (e: Exception) {
            throw e
        } finally {
            ta?.recycle()
        }

        editableVisor.isEnabled = isEditable
    }

    private fun rotate(rotationMode: RotationMode) {
        val angle = if (rotationMode == RotationMode.Counterclockwise) -90f else 90f
        editableVisor.rotate(angle)
    }

    fun setImageUri(uri: Uri) = editableVisor.setImageUri(uri)

    fun getCroppedBitmap(): Bitmap? {
        return try {
            // Get the bitmap from the EditableVisor (assuming it has a method to provide the current image)
            val originalBitmap = editableVisor.getImageBitmap() ?: return null

            //Create a bitmap to draw on with the visible size of ComposedVisor
            val croppedBitmap = createBitmap(width, height)
            val canvas = Canvas(croppedBitmap)

            // Calculate the matrix to apply on the original bitmap to draw the visible part into croppedBitmap
            val drawMatrix = Matrix().apply {
                //We need to translate the image to the top-left corner of the view
                postTranslate(-editableVisor.left.toFloat(), -editableVisor.top.toFloat())
                //Apply the transformation matrix of the EditableVisor to draw only the visible part
                postConcat(editableVisor.getImageMatrix())
            }

            canvas.drawBitmap(originalBitmap, drawMatrix, null)
            croppedBitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}