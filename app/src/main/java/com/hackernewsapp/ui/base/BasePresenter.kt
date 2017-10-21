package com.hackernewsapp.ui.base

/**
 * @author Tosin Onikute.
 */

open class BasePresenter<T : MvpView> : MvpPresenter<T> {

    var mvpView: T? = null
        private set

    val isViewAttached: Boolean
        get() = mvpView != null

    override fun attachView(mvpView: T) {
        this.mvpView = mvpView
    }

    override fun detachView() {
        mvpView = null
    }

    fun checkViewAttached() {
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

    class MvpViewNotAttachedException : RuntimeException("Please call Presenter.onAttach(MvpView) before requesting data to the Presenter")
}