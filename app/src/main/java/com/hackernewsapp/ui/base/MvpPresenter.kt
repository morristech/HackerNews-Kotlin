package com.hackernewsapp.ui.base

/**
 * @author Tosin Onikute.
 */

interface MvpPresenter<V : MvpView> {

    fun attachView(mvpView: V)

    fun detachView()
}