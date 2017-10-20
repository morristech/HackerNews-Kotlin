package com.hackernewsapp.util

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.text.format.DateUtils
import android.widget.TextView
import android.widget.Toast

/**
 * @author Tosin Onikute.
 */

class Misc(private val mContext: Context) {
    companion object {

        fun formatTime(hnTimestamp: Long): CharSequence {
            var hnTimestamp = hnTimestamp
            hnTimestamp = 1000 * hnTimestamp
            return DateUtils.getRelativeTimeSpanString(hnTimestamp,
                    System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS)
        }

        fun displayLongToast(context: Context, text: CharSequence) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }

        fun displayLongToast(context: Context, @StringRes ResId: Int) {
            Toast.makeText(context, ResId, Toast.LENGTH_LONG).show()
        }

        fun setSnackBarTextColor(snackbar: Snackbar, context: Context, @ColorRes color: Int) {
            val snackbarText = snackbar.view.findViewById(android.support.design.R.id.snackbar_text) as TextView
            snackbarText.setTextColor(context.resources.getColor(color))
        }
    }


}
