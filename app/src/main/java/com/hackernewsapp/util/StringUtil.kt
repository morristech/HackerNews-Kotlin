package com.hackernewsapp.util

import android.content.Context
import java.util.*
import java.util.regex.Pattern

/**
 * @author Tosin Onikute.
 */

class StringUtil(private val mContext: Context) {
    companion object {

        fun isBlank(string: CharSequence?): Boolean {
            return string == null || string.toString().trim { it <= ' ' }.length == 0
        }

        fun toTitleCase(str: String?): String? {
            if (str == null) {
                return null
            }

            var space = true
            val builder = StringBuilder(str)
            val len = builder.length

            for (i in 0 until len) {
                val c = builder[i]
                if (space) {
                    if (!Character.isWhitespace(c)) {
                        // Convert to title case and switch out of whitespace mode.
                        builder.setCharAt(i, Character.toTitleCase(c))
                        space = false
                    }
                } else if (Character.isWhitespace(c)) {
                    space = true
                } else {
                    builder.setCharAt(i, Character.toLowerCase(c))
                }
            }

            return builder.toString()
        }

        fun extractUrls(text: String): List<String> {
            val containedUrls = ArrayList<String>()
            val urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"
            val pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE)
            val urlMatcher = pattern.matcher(text)

            while (urlMatcher.find()) {
                containedUrls.add(text.substring(urlMatcher.start(0),
                        urlMatcher.end(0)))
            }

            return containedUrls
        }
    }
}
