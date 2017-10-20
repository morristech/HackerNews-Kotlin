package com.hackernewsapp.util.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.Property
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator


class CircularProgressDrawable(color: Int, private val mBorderWidth: Float) : Drawable(), Animatable {
    private val fBounds = RectF()

    private var mObjectAnimatorSweep: ObjectAnimator? = null
    private var mObjectAnimatorAngle: ObjectAnimator? = null
    private var mModeAppearing: Boolean = false
    private var mCurrentGlobalAngleOffset: Float = 0.toFloat()
    var currentGlobalAngle: Float = 0.toFloat()
        set(currentGlobalAngle) {
            field = currentGlobalAngle
            invalidateSelf()
        }
    var currentSweepAngle: Float = 0.toFloat()
        set(currentSweepAngle) {
            field = currentSweepAngle
            invalidateSelf()
        }
    private val mPaint: Paint
    private var mRunning: Boolean = false

    //create Property Objects for mutable values AngleProperty and SweepProperty
    //refer to: http://developer.android.com/reference/android/util/Property.html

    private val mAngleProperty = object : Property<CircularProgressDrawable, Float>(Float::class.java, "angle") {
        override fun get(`object`: CircularProgressDrawable): Float {
            return `object`.currentSweepAngle
        }

        override fun set(`object`: CircularProgressDrawable, value: Float?) {
            `object`.currentGlobalAngle = value!!
        }
    }

    private val mSweepProperty = object : Property<CircularProgressDrawable, Float>(Float::class.java, "sweep") {
        override fun get(`object`: CircularProgressDrawable): Float {
            return `object`.currentGlobalAngle
        }

        override fun set(`object`: CircularProgressDrawable, value: Float?) {
            `object`.currentSweepAngle = value!!
        }
    }

    init {

        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mBorderWidth
        mPaint.color = color

        setupAnimations()
    }

    override fun start() {
        if (isRunning) {
            return
        }
        mRunning = true
        mObjectAnimatorAngle!!.start()
        mObjectAnimatorSweep!!.start()
        invalidateSelf()
    }

    override fun stop() {
        if (!isRunning) {
            return
        }
        mRunning = false
        mObjectAnimatorAngle!!.cancel()
        mObjectAnimatorSweep!!.cancel()
        invalidateSelf()
    }

    override fun isRunning(): Boolean {
        return mRunning
    }

    override fun draw(canvas: Canvas) {
        var startAngle = currentGlobalAngle - mCurrentGlobalAngleOffset
        var sweepAngle = currentSweepAngle

        if (!mModeAppearing) {
            startAngle = startAngle + sweepAngle
            sweepAngle = 360f - sweepAngle - MIN_SWEEP_ANGLE.toFloat()
        } else {
            sweepAngle += MIN_SWEEP_ANGLE.toFloat()
        }
        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint)
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        fBounds.left = bounds.left.toFloat() + mBorderWidth / 2f + .5f
        fBounds.right = bounds.right.toFloat() - mBorderWidth / 2f - .5f
        fBounds.top = bounds.top.toFloat() + mBorderWidth / 2f + .5f
        fBounds.bottom = bounds.bottom.toFloat() - mBorderWidth / 2f - .5f
    }

    private fun toggleAppearingMode() {
        mModeAppearing = !mModeAppearing
        if (mModeAppearing) {
            mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360
        }
    }

    private fun setupAnimations() {
        mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, mAngleProperty, 360f)
        mObjectAnimatorAngle!!.interpolator = ANGLE_INTERPOLATOR
        mObjectAnimatorAngle!!.duration = ANGLE_ANIMATOR_DURATION.toLong()
        mObjectAnimatorAngle!!.repeatMode = ValueAnimator.RESTART
        mObjectAnimatorAngle!!.repeatCount = ValueAnimator.INFINITE

        mObjectAnimatorSweep = ObjectAnimator.ofFloat(this, mSweepProperty, 360f - MIN_SWEEP_ANGLE * 2)
        mObjectAnimatorSweep!!.interpolator = SWEEP_INTERPOLATOR
        mObjectAnimatorSweep!!.duration = SWEEP_ANIMATOR_DURATION.toLong()
        mObjectAnimatorSweep!!.repeatMode = ValueAnimator.RESTART
        mObjectAnimatorSweep!!.repeatCount = ValueAnimator.INFINITE
        mObjectAnimatorSweep!!.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {}

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {
                toggleAppearingMode()
            }
        })
    }

    companion object {

        private val ANGLE_INTERPOLATOR = LinearInterpolator()
        private val SWEEP_INTERPOLATOR = DecelerateInterpolator()
        private val ANGLE_ANIMATOR_DURATION = 2000
        private val SWEEP_ANIMATOR_DURATION = 600
        private val MIN_SWEEP_ANGLE = 30
    }
}
