package com.hackernewsapp.ui.base

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.hackernewsapp.R

/**
 * @author Tosin Onikute.
 */

abstract class BaseActivity : AppCompatActivity() {

    var snackbarOffline: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    abstract fun loadView()

    fun displayOfflineSnackbar() {
        snackbarOffline = Snackbar.make(findViewById(android.R.id.content), R.string.no_connection_snackbar, Snackbar.LENGTH_INDEFINITE)
        val snackbarText = snackbarOffline!!.view.findViewById(android.support.design.R.id.snackbar_text) as TextView
        snackbarText.setTextColor(applicationContext.resources.getColor(android.R.color.white))
        snackbarOffline!!.setAction(R.string.snackbar_action_retry) { loadView()  }
        snackbarOffline!!.setActionTextColor(resources.getColor(R.color.colorPrimary))
        snackbarOffline!!.show()
    }

    fun hideOfflineSnackBar() {
        if (snackbarOffline != null && snackbarOffline!!.isShown) {
            snackbarOffline!!.dismiss()
        }
    }

    fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }


}
