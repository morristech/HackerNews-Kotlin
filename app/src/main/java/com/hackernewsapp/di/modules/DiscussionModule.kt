package com.hackernewsapp.di.modules

import android.app.Application

import com.hackernewsapp.discussion.data.DiscussionInteractor
import com.hackernewsapp.discussion.data.DiscussionInteractorImpl
import com.hackernewsapp.discussion.presenter.DiscussionPresenter
import com.hackernewsapp.discussion.presenter.DiscussionPresenterImpl

import dagger.Module
import dagger.Provides

/**
 * Created by tosin on 3/13/2017.
 */

@Module
class DiscussionModule(private val application: Application) {

    @Provides
    fun getDiscussionPresenter(discussionInteractor: DiscussionInteractor): DiscussionPresenter {
        return DiscussionPresenterImpl(application, discussionInteractor)
    }

    @Provides
    fun providesDiscussionInteractor(): DiscussionInteractor {
        return DiscussionInteractorImpl(application)
    }
}
