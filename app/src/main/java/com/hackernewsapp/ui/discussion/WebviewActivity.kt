// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.hackernewsapp.ui.discussion

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

import com.hackernewsapp.R
import com.hackernewsapp.util.Misc
import com.hackernewsapp.util.ui.MaterialProgressBar

/**
 * This Activity is used as a fallback when there is no browser installed that supports
 * Chrome Custom Tabs
 */
class WebviewActivity : AppCompatActivity() {

    var EXTRA_URL = ""
    private val url: String? = null
    private var progressBar: MaterialProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        var url: String? = intent.getStringExtra(EXTRA_URL)
        progressBar = findViewById(R.id.material_progress_bar) as MaterialProgressBar
        val extras = intent.extras
        if (extras != null) {
            url = extras.getString("EXTRA_URL")
        }

        if (url != null) {
            loadWebView(url)
        }

    }

    fun loadWebView(url: String) {

        progressBar!!.visibility = View.VISIBLE
        title = url
        val webview = findViewById(R.id.webview) as WebView
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true

        webview.setWebViewClient(object : WebViewClient() {
            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                Misc.displayLongToast(applicationContext, description)
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {

            }

            override fun onPageFinished(view: WebView, url: String) {
                progressBar!!.visibility = View.GONE
                webview.visibility = View.VISIBLE
            }
        })
        webview.setWebChromeClient(WebChromeClient())
        webview.loadUrl(url)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
