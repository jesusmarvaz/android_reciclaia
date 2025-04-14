package com.ingencode.reciclaia.ui.components.visor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.graphics.withMatrix
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlin.math.min

/**
Created with ❤ by jesusmarvaz on 2025-04-09.
 */

class EditableVisor: View {
    private var imageBitmap: Bitmap? = null
    private var matrix = Matrix()
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private val paint = Paint()
    private var imageRect: RectF? = null
    private var viewRect: RectF? = null
    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init() }

    private fun init() {
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        gestureDetector = GestureDetector(context, GestureListener())
        setOnTouchListener(TouchListener())
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            matrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
            invalidate()
            return true
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() { // GestureListener
        override fun onDoubleTap(e: MotionEvent): Boolean {
            resetZoom()
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            performClick()
            return true
        }
    }

    private inner class TouchListener : OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            scaleGestureDetector.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = event.x
                    lastTouchY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY
                    matrix.postTranslate(dx, dy)
                    lastTouchX = event.x
                    lastTouchY = event.y
                    invalidate()
                }
            }
            performClick()
            return true
        }
    }

    private fun resetZoom() {
        imageBitmap?.let { bitmap ->
            val scale: Float = min(width.toFloat() / bitmap.width, height.toFloat() / bitmap.height)
            matrix.reset()
            matrix.postScale(scale, scale, width / 2f, height / 2f)
            invalidate()
        }
    }

    fun rotate(degrees: Float) {
        val centerX = width / 2f
        val centerY = height / 2f
        matrix.postRotate(degrees, centerX, centerY)
        invalidate()
    }

    fun setImageUri(uri: Uri) {
        Glide.with(this)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    imageBitmap = resource
                    imageRect = RectF(0f, 0f, resource.width.toFloat(), resource.height.toFloat())
                    viewRect = RectF(0f, 0f, width.toFloat(), height.toFloat())
                    matrix = Matrix() // Reset matrix for the new image
                    // Calculate initial scale to fit the image within the view.
                    val scale: Float = min(width.toFloat() / resource.width, height.toFloat() / resource.height)
                    matrix.postScale(scale, scale, width / 2f, height / 2f)
                    invalidate()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    imageBitmap = null
                    invalidate()
                }
            })
    }

    fun getImageMatrix(): Matrix = this.matrix

    fun getImageBitmap(): Bitmap? = this.imageBitmap

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        imageBitmap?.let { bitmap ->
            canvas.withMatrix(matrix) {
                imageRect?.let {
                    drawBitmap(bitmap, null, it, paint)
                }
            }
        }
    }
}