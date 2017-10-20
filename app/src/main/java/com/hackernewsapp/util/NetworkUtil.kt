package com.hackernewsapp.util

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager

/**
 * @author Tosin Onikute.
 */

object NetworkUtil {

    // this is to check either connected or Wifi or Mobile data, NetworkUtil.isOnNetwork(this, ConnectivityManager.TYPE_WIFI)
    fun isOnNetwork(context: Context, networkType: Int): Boolean {
        var isConnected = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetworkInfo
        if (activeNetwork != null) {
            if (activeNetwork.type == networkType) {
                isConnected = true
            }
        }
        return isConnected
    }

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return if (ni == null) {
            // There are no active networks.
            false
        } else {
            true
        }
    }


}
