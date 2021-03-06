package com.hackernewsapp.ui.discussion.presenter

import android.app.Application
import android.content.Context
import com.hackernewsapp.StoryInterface
import com.hackernewsapp.data.DiscussionInteractor
import com.hackernewsapp.data.model.Discussion
import com.hackernewsapp.data.model.Story
import com.hackernewsapp.ui.base.BasePresenter
import com.hackernewsapp.ui.discussion.view.DiscussionView
import com.hackernewsapp.util.Logger
import com.hackernewsapp.util.NetworkUtil
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

/**
 * @author Tosin Onikute.
 */

class DiscussionPresenter(private val application: Application, private val discussionInteractor: DiscussionInteractor) : BasePresenter<DiscussionView>() {


    private val logger = Logger.getLogger(javaClass)
    private var discussionView: DiscussionView? = null

    private var mCommentListObservable: Observable<List<Discussion>>? = null
    private var discussionArrayList: ArrayList<Discussion>? = null

    override fun attachView(discussionView: DiscussionView) {
        super.attachView(discussionView)
    }

    override fun detachView() {
        super.detachView()
    }

    fun getComments(mInterface: StoryInterface?, mCompositeSubscription: CompositeSubscription?, context: Context, story: Story?, updateObservable: Boolean) {
        if (!NetworkUtil.isConnected(context)) {
            discussionView!!.displayOfflineSnackbar()
            return
        }

        if(isViewAttached) {
            discussionView!!.showLoading()
        }

        if (story!!.kids != null && !story!!.kids!!.isEmpty()) {
            if (mCommentListObservable == null || updateObservable) {
                mCommentListObservable = discussionInteractor.fetchComment(mInterface!!, 0, story).cache()
            }

            mCompositeSubscription?.add(mCommentListObservable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<List<Discussion>>() {
                        override fun onCompleted() {

                            if(isViewAttached) {
                                discussionView!!.hideLoading()
                                discussionView!!.setAdapter(discussionArrayList!!)
                            }
                        }

                        override fun onError(thr: Throwable) {
                            logger.debug(thr.message.toString())
                            discussionView!!.hideLoading()
                        }

                        override fun onNext(listDiscussion: List<Discussion>?) {

                            if (listDiscussion != null) {
                                discussionArrayList = ArrayList(listDiscussion)
                            }

                        }
                    })
            )
        } else {
            // update the view to say no comment yet
            discussionView!!.sayNoComment()

        }
    }


}
