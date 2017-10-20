package com.hackernewsapp.ui.discussion.presenter

import android.content.Context

import com.hackernewsapp.StoryInterface
import com.hackernewsapp.ui.discussion.view.DiscussionView
import com.hackernewsapp.data.model.Story

import rx.subscriptions.CompositeSubscription

/**
 * @author Tosin Onikute.
 */

interface DiscussionPresenter {

    fun setView(discussionView: DiscussionView)

    fun getComments(mInterface: StoryInterface?, mCompositeSubscription: CompositeSubscription?,
                    context: Context, story: Story?, updateObservable: Boolean)


}
