package com.hackernewsapp.util

import android.util.Log

/**
 * @author Tosin Onikute.
 */

class Logger {

    internal val tag: String
    var isEnable = true

    constructor(tag: String) {
        this.tag = tag
    }

    constructor(cls: Class<*>) {
        this.tag = cls.name
    }

    fun debug(msg: String) {
        if (isEnable) {
            Log.d(tag, msg)
        }
    }

    fun debug(msg: String, vararg args: Any) {
        debug(String.format(msg, *args))
    }

    fun warn(msg: String) {
        if (isEnable) {
            Log.w(tag, msg)
        }
    }

    fun warn(throwable: Throwable) {
        warn(Log.getStackTraceString(throwable))
    }

    fun warn(msg: String, vararg args: Any) {
        warn(String.format(msg, *args))
    }

    fun info(msg: String) {
        if (isEnable) {
            Log.i(tag, msg)
        }
    }

    fun info(msg: String, vararg args: Any) {
        info(String.format(msg, *args))
    }

    fun error(msg: String) {
        if (isEnable) {
            Log.e(tag, msg)
        }
    }

    fun error(throwable: Throwable) {
        error(Log.getStackTraceString(throwable))
    }

    fun error(msg: String, vararg args: Any) {
        error(String.format(msg, *args))
    }

    fun verbose(msg: String) {
        if (isEnable) {
            Log.v(tag, msg)
        }
    }

    fun verbose(msg: String, vararg args: Any) {
        verbose(String.format(msg, *args))
    }

    companion object {

        fun getLogger(cls: Class<*>): Logger {
            return Logger(cls)
        }

        fun getLogger(tag: String): Logger {
            return Logger(tag)
        }
    }
}
