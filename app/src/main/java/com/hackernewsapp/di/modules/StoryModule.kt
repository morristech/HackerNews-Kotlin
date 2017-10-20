package com.hackernewsapp.di.modules


import android.app.Application

import com.hackernewsapp.story.data.StoryInteractor
import com.hackernewsapp.story.data.StoryInteractorImpl
import com.hackernewsapp.story.presenter.StoryPresenter
import com.hackernewsapp.story.presenter.StoryPresenterImpl

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