package com.hackernewsapp.ui.discussion.view

import android.view.View
import com.hackernewsapp.data.model.Discussion
import com.hackernewsapp.data.model.Story
import com.hackernewsapp.ui.base.MvpView
import java.util.*

/**
 * Created by tosin on 3/13/2017.
 */

interface DiscussionView : MvpView {

    fun init()

    fun setCommentHeader(story: Story?)

    fun setCollapseToolbar(title: String)

    fun displayOfflineSnackbar()

    fun sayNoComment()

    fun setAdapter(discussionArrayList: ArrayList<Discussion>)

    fun fabButtonSetup()

    fun fabButtonLink(v: View?)

    fun shareLink()

}
