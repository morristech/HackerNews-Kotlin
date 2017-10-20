package com.hackernewsapp.discussion.view

import android.view.View
import com.hackernewsapp.discussion.model.Discussion
import com.hackernewsapp.story.model.Story
import java.util.*

/**
 * Created by tosin on 3/13/2017.
 */

interface DiscussionView {

    fun init()

    fun loadView()

    fun setCommentHeader(story: Story?)

    fun setCollapseToolbar(title: String)

    fun displayOfflineSnackbar()

    fun setProgressBarVisible()

    fun setProgressBarGone()

    fun sayNoComment()

    fun setAdapter(discussionArrayList: ArrayList<Discussion>)

    fun fabButtonSetup()

    fun fabButtonLink(v: View?)

    fun shareLink()

}
