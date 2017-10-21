package com.hackernewsapp.ui.story.view

import com.hackernewsapp.data.model.Story
import com.hackernewsapp.ui.base.MvpView
import java.util.*

/**
 * Created by tosin on 3/13/2017.
 */

interface StoryView  : MvpView {

    fun init()

    fun populateRecyclerView()

    fun pullToRefresh()

    fun refresh(topStories: String, refresh: Boolean)

    fun implementScrollListener()

    fun setLayoutVisibility()

    fun displayOfflineSnackbar()

    fun hideOfflineSnackBar()

    fun doAfterFetchStory()

    fun setAdapter(storLoaded: Int?, listArrayList: ArrayList<Story>,
                   refreshedArrayList: ArrayList<Story>, loadmore: Boolean, totalNum: Int?)

}
