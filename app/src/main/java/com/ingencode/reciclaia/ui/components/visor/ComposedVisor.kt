package com.ingencode.reciclaia.ui.components.visor

import android.content.Context
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
    constructor(context: Context): super(context) { initialize() }
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs){ initialize() }
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle){ initialize() }

    private lateinit var editableVisor: EditableVisor
    private lateinit var clockWiseButton: ImageButton
    private lateinit var counterClockWiseButton: ImageButton

    private fun initialize(/*attrs: AttributeSet? = null*/) {
        val li = LayoutInflater.from(context)
        li.inflate(R.layout.composed_visor_layout, this, true)
        editableVisor = findViewById<EditableVisor>(R.id.editable_visor)

        clockWiseButton = findViewById<ImageButton>(R.id.iv_clockwise)
        clockWiseButton.setOnClickListener { rotate(RotationMode.Clockwise) }

        counterClockWiseButton = findViewById<ImageButton>(R.id.iv_counterclockwise)
        counterClockWiseButton.setOnClickListener { rotate(RotationMode.Counterclockwise) }
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