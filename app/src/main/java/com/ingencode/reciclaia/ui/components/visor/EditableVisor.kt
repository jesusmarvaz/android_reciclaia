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

    private var activePointerId = MotionEvent.INVALID_POINTER_ID
    private var isScaling = false

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init() }

    private fun init() {
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        gestureDetector = GestureDetector(context, GestureListener())
        setOnTouchListener(TouchListener())
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            isScaling = true
            return super.onScaleBegin(detector)
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            isScaling = false
            super.onScaleEnd(detector)
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if (!isEnabled) return false
            val scaleFactor = detector.scaleFactor
            matrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
            invalidate()
            return true
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            resetZoom()
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return false
        }
    }

    private inner class TouchListener : OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (!isEnabled) return false
            scaleGestureDetector.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = event.x
                    lastTouchY = event.y
                    activePointerId = event.getPointerId(0)
                }

                MotionEvent.ACTION_POINTER_DOWN -> {
                    if (!isScaling) {
                        val pointerIndex = event.actionIndex
                        lastTouchX = event.getX(pointerIndex)
                        lastTouchY = event.getY(pointerIndex)
                        activePointerId = event.getPointerId(pointerIndex)
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    val pointerIndex = event.findPointerIndex(activePointerId)
                    if (pointerIndex == -1) return true

                    val x = event.getX(pointerIndex)
                    val y = event.getY(pointerIndex)

                    if (!scaleGestureDetector.isInProgress) {
                        val dx = x - lastTouchX
                        val dy = y - lastTouchY
                        matrix.postTranslate(dx, dy)
                        invalidate()
                    }

                    lastTouchX = x
                    lastTouchY = y
                }

                MotionEvent.ACTION_POINTER_UP -> {
                    val pointerIndex = event.actionIndex
                    val pointerId = event.getPointerId(pointerIndex)
                    if (pointerId == activePointerId) {
                        val newPointerIndex = if (pointerIndex == 0) 1 else 0
                        lastTouchX = event.getX(newPointerIndex)
                        lastTouchY = event.getY(newPointerIndex)
                        activePointerId = event.getPointerId(newPointerIndex)
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    activePointerId = MotionEvent.INVALID_POINTER_ID
                }
            }
            performClick()
            return true
        }
    }

    private fun resetZoom() {
        if (!isEnabled) return
        imageBitmap?.let { bitmap ->
            val scale: Float = min(width.toFloat() / bitmap.width, height.toFloat() / bitmap.height)
            matrix.reset()
            matrix.postScale(scale, scale)
            val scaledWidth = bitmap.width * scale
            val scaledHeight = bitmap.height * scale
            val dx = (width - scaledWidth) / 2f
            val dy = (height - scaledHeight) / 2f
            matrix.postTranslate(dx, dy)
            invalidate()
        }
    }

    fun rotate(degrees: Float) {
        if (!isEnabled) return
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
                    matrix = Matrix()
                    val scale: Float = min(width.toFloat() / resource.width, height.toFloat() / resource.height)
                    matrix.postScale(scale, scale)
                    val scaledWidth = resource.width * scale
                    val scaledHeight = resource.height * scale
                    val dx = (width - scaledWidth) / 2f
                    val dy = (height - scaledHeight) / 2f
                    matrix.postTranslate(dx, dy)

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