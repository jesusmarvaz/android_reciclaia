package com.ingencode.reciclaia.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.animation.with
import android.graphics.Matrix
import com.bumptech.glide.Glide

/**
Created with ❤ by jesusmarvaz on 2025-04-08.
 */

class EditableImageView : AppCompatImageView {
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var matrix = Matrix()
    private var lastTouchX = 0f
    private var lastTouchY = 0f

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        scaleType = ScaleType.MATRIX
        imageMatrix = matrix
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        setOnTouchListener(TouchListener())
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            matrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
            imageMatrix = matrix
            invalidate()
            return true
        }
    }

    private inner class TouchListener : OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            scaleGestureDetector.onTouchEvent(event)
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
                    imageMatrix = matrix
                    invalidate()
                }
            }
            return true
        }
    }

    fun rotate(degrees: Float) {
        val centerX = width / 2f
        val centerY = height / 2f
        matrix.postRotate(degrees, centerX, centerY)
        imageMatrix = matrix
        invalidate()
    }

    fun setImageUri(uri: Uri) {
        Glide.with(this).load(uri).into(this)
    }
}