package com.hackernewsapp.discussion.data

import com.hackernewsapp.StoryInterface
import com.hackernewsapp.discussion.model.Discussion
import com.hackernewsapp.story.model.Story
import rx.Observable
import java.util.*

/**
 * @author Tosin Onikute.
 *
 * StoryInteractor is an interface that is implemented by the StoryInteractorImpl Data Manager
 */

interface DiscussionInteractor {

    fun fetchComment(mInterface: StoryInterface, level: Int, story: Story): Observable<List<Discussion>>

    fun getPartsComment(mInterface: StoryInterface, level: Int, cmtIds: List<Long>): Observable<List<Discussion>>

    fun getSinglePartComments(mInterface: StoryInterface, level: Int, cmtId: Long): Observable<List<Discussion>>

    fun getAllComments(mInterface: StoryInterface, level: Int, firstLevelCmtIds: List<Long>): Observable<List<Discussion>>

    fun getInnerLevelComments(mInterface: StoryInterface, level: Int, cmt: Discussion): Observable<Discussion>?

    fun sortComments(allDiscussions: List<Discussion>, firstLevelCmtIds: List<Long>): List<Discussion>

    fun sortAllComments(allCommentsHashMap: HashMap<Long, Discussion>, listOfDiscussions: List<Discussion>): List<Discussion>


}
