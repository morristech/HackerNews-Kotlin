package com.hackernewsapp.ui.story.presenter

import com.hackernewsapp.StoryInterface
import com.hackernewsapp.ui.story.view.StoryView

import rx.subscriptions.CompositeSubscription

/**
 * @author Tosin Onikute.
 */

interface StoryPresenter {

    fun setView(storyView: StoryView)

    fun updateRecyclerView(storyInterface: StoryInterface, mCompositeSubscription: CompositeSubscription,
                           fromIndex: Int?, toIndex: Int?)

    fun getStoryIds(storyInterface: StoryInterface, storyTypeUrl: String,
                    mCompositeSubscription: CompositeSubscription, refresh: Boolean)


}