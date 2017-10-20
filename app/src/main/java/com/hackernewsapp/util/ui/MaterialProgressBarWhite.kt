package com.hackernewsapp.util.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View

import com.hackernewsapp.R


class MaterialProgressBarWhite @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val mCircleDrawable: CircularProgressDrawable?

    init {

        //TODO: setup deprecate proof color extraction
        val color = context.resources.getColor(R.color.colorWhite)

        mCircleDrawable = CircularProgressDrawable(color, 8f)
        mCircleDrawable.callback = this
        if (visibility == View.VISIBLE) {
            mCircleDrawable.start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mCircleDrawable?.draw(canvas)
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (mCircleDrawable != null) {
            if (visibility == View.VISIBLE) {
                mCircleDrawable.start()
            } else {
                mCircleDrawable.stop()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCircleDrawable?.setBounds(0, 0, w, h)
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return who === mCircleDrawable || super.verifyDrawable(who)
    }
}
