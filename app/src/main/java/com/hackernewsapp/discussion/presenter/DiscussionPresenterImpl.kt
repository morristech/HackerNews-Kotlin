package com.hackernewsapp.discussion.presenter

import android.app.Application
import android.content.Context
import com.hackernewsapp.StoryInterface
import com.hackernewsapp.discussion.data.DiscussionInteractor
import com.hackernewsapp.discussion.model.Discussion
import com.hackernewsapp.discussion.view.DiscussionView
import com.hackernewsapp.story.model.Story
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

class DiscussionPresenterImpl(private val application: Application, private val discussionInteractor: DiscussionInteractor) : DiscussionPresenter {


    private val logger = Logger.getLogger(javaClass)
    private var discussionView: DiscussionView? = null

    private var mCommentListObservable: Observable<List<Discussion>>? = null
    private var discussionArrayList: ArrayList<Discussion>? = null

    override fun setView(discussionView: DiscussionView) {
        this.discussionView = discussionView
    }


    override fun getComments(mInterface: StoryInterface?, mCompositeSubscription: CompositeSubscription?, context: Context, story: Story?, updateObservable: Boolean) {
        if (!NetworkUtil.isConnected(context)) {
            discussionView!!.displayOfflineSnackbar()
            return
        }

        discussionView!!.setProgressBarVisible()

        if (story!!.kids != null && !story!!.kids!!.isEmpty()) {
            if (mCommentListObservable == null || updateObservable) {
                mCommentListObservable = discussionInteractor.fetchComment(mInterface!!, 0, story).cache()
            }

            mCompositeSubscription?.add(mCommentListObservable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<List<Discussion>>() {
                        override fun onCompleted() {

                            discussionView!!.setProgressBarGone()
                            discussionView!!.setAdapter(discussionArrayList!!)
                        }

                        override fun onError(thr: Throwable) {
                            logger.debug(thr.message.toString())
                            discussionView!!.setProgressBarGone()
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
