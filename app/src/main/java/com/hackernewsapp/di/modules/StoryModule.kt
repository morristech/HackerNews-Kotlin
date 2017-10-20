package com.hackernewsapp.di.modules


import android.app.Application

import com.hackernewsapp.data.StoryInteractor
import com.hackernewsapp.data.StoryInteractorImpl
import com.hackernewsapp.ui.story.presenter.StoryPresenter
import com.hackernewsapp.ui.story.presenter.StoryPresenterImpl

import dagger.Module
import dagger.Provides

/**
 * @author Tosin Onikute.
 */

@Module
class StoryModule(private val application: Application) {

    @Provides
    fun getStoryPresenter(storyInteractor: StoryInteractor): StoryPresenter {
        return StoryPresenterImpl(application, storyInteractor)
    }

    @Provides
    fun providesStoryInteractor(): StoryInteractor {
        return StoryInteractorImpl(application)
    }


}