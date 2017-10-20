package com.hackernewsapp.data

import com.hackernewsapp.StoryInterface
import com.hackernewsapp.data.model.Story

import rx.Observable

/**
 * @author Tosin Onikute.
 *
 * StoryInteractor is an interface that is implemented by the StoryInteractorImpl Data Manager
 */

interface StoryInteractor {

    fun sayHello()

    fun getStorys(storyInterface: StoryInterface, storyIds: List<Long>): Observable<Story>

    fun subListStories(storyInterface: StoryInterface, storyIds: List<Long>): Observable<List<Story>>

    fun sortStories(storyList: List<Story>, storyIds: List<Long>): List<Story>


}
